package net.cloudengine.new_.cti.asterisk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cloudengine.new_.cti.EventListener;
import net.cloudengine.new_.cti.EventProvider;
import net.cloudengine.new_.cti.model.Call;
import net.cloudengine.new_.cti.model.Extension;
import net.cloudengine.new_.cti.model.QEntry;
import net.cloudengine.new_.cti.model.Status;
import net.cloudengine.new_.cti.model.asterisk.AbstractCallAsteriskImpl;
import net.cloudengine.new_.cti.model.asterisk.CallCreator;
import net.cloudengine.new_.cti.model.asterisk.ExtensionAsteriskImpl;
import net.cloudengine.new_.cti.model.asterisk.QEntryAsteriskImpl;
import net.cloudengine.new_.cti.model.asterisk.QMemberAsteriskImpl;
import net.cloudengine.new_.cti.model.asterisk.QueueAsteriskImpl;
import net.cloudengine.new_.cti.model.asterisk.SinglePartyCallImpl;
import net.cloudengine.new_.cti.model.asterisk.TwoPartyCallImpl;

import org.asteriskjava.manager.AbstractManagerEventListener;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ExtensionStateAction;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.IaxPeerListAction;
import org.asteriskjava.manager.action.ManagerAction;
import org.asteriskjava.manager.action.MonitorAction;
import org.asteriskjava.manager.action.QueueStatusAction;
import org.asteriskjava.manager.action.SipPeersAction;
import org.asteriskjava.manager.action.StatusAction;
import org.asteriskjava.manager.event.ExtensionStatusEvent;
import org.asteriskjava.manager.event.JoinEvent;
import org.asteriskjava.manager.event.LeaveEvent;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.event.NewChannelEvent;
import org.asteriskjava.manager.event.PeerEntryEvent;
import org.asteriskjava.manager.event.PeerlistCompleteEvent;
import org.asteriskjava.manager.event.QueueEntryEvent;
import org.asteriskjava.manager.event.QueueEvent;
import org.asteriskjava.manager.event.QueueMemberAddedEvent;
import org.asteriskjava.manager.event.QueueMemberEvent;
import org.asteriskjava.manager.event.QueueMemberRemovedEvent;
import org.asteriskjava.manager.event.QueueParamsEvent;
import org.asteriskjava.manager.event.StatusEvent;
import org.asteriskjava.manager.response.ExtensionStateResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsteriskEventProvider extends AbstractManagerEventListener implements EventProvider, EventContextProvider {

	private static final Logger logger = LoggerFactory.getLogger(AsteriskEventProvider.class);
	
	private List<String> sipList = new ArrayList<String>();
	private List<String> aixList = new ArrayList<String>();
	
	private ManagerConnection manager = null;
	
	private Map<String,String> queueEntries = new HashMap<String, String>();
	private List<EventListener> listeners = new ArrayList<EventListener>();
	
	private List<CallCreator> creators = new ArrayList<CallCreator>();
	private List<AbstractCallAsteriskImpl> calls = new ArrayList<AbstractCallAsteriskImpl>();
	private Map<String,StatusEvent> channels = new HashMap<String, StatusEvent>();

	private ExecutorService es = Executors.newSingleThreadExecutor();
	private AtomicInteger count = new AtomicInteger(0);
	
	@Override
	public void addListener(EventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(EventListener listener) {
		listeners.remove(listener);
	}
	
	// ~~ Gestión de la conexión ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	
	public void connectionEstablished(ManagerConnection manager) {
		if (logger.isDebugEnabled()) {
			String hostname = manager.getHostname();
			logger.debug("Conexión establecida con servidor asterisk, host: {}", hostname);
		}
		
		cleanUp();
		this.manager = manager;
		this.manager.addEventListener(this);
		for (EventListener listener : listeners) {
			listener.onConnect();
		}
		
		sendAction(new StatusAction());
		sendAction(new QueueStatusAction());
		sendAction(new SipPeersAction());
		sendAction(new IaxPeerListAction());

	}
	
	public void connectionInterrupted(ManagerConnection manager) {
		if (logger.isDebugEnabled()) {
			logger.debug("Conexión interrumpida con servidor asterisk");
		}
		
		cleanUp();
		for (EventListener listener : listeners) {
			listener.onDisconnect();
		}
	}
	
	/**
	 * Borra toda la información de estado ante una desconexión.
	 */
	private void cleanUp() {
		sipList.clear();
		aixList.clear();
		creators.clear();
		calls.clear();
		queueEntries.clear();
		channels.clear();
		count.set(0);
	}
	
	// ~~ Gestión de las llamadas ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public void onManagerEvent(ManagerEvent event) {
		super.onManagerEvent(event);
		
		boolean processed = false;
		for(AbstractCallAsteriskImpl call : calls) {
			processed =  call.handleEvent(event, this);
			if (processed) {
				break;
			}
		}
		
//		for (AbstractAsteriskCall call : queuedCalls) {
//			processed = call.handleEvent(event, context);
//			if (processed) {
//				break;
//			}
//		}

		if (!processed) {
			processEvent(event);
		}
	}

	private void processEvent(ManagerEvent event) {
		boolean processed = false;

		for(CallCreator creator : creators) {
			processed = creator.handleEvent(event, this);
			if (processed) {
				if (!creator.isValid()) {
					System.out.println("Elimino CallBuilder");
					creators.remove(creator);
				}
				break;
			}
		}

		if (!processed) {
			if (event instanceof NewChannelEvent) {
				NewChannelEvent nce = (NewChannelEvent) event;
				CallCreator creator = new CallCreator(nce);
				creators.add(creator);
			}
		}
	}
	
	
	/** Notifica la existencia de los channels de asterisk.
	 * Se utiliza para generar las llamadas activas.
	 * Si es una llamada entre dos participantes hay dos channels
	 * para esa llamada. Si es una llamada con un solo participante hay
	 * un solo channel.
	 */
	@Override
	@SuppressWarnings("all")
	public void handleEvent(StatusEvent event) {
		
		String callerId = event.getCallerId();
		String ext = event.getExtension();
		
		if (event.getBridgedUniqueId() != null) {
			
			// busco el evento relacionado
			StatusEvent se2 = channels.get(event.getBridgedUniqueId());
			if (se2 != null) {
				
				if (logger.isDebugEnabled()) {
					logger.debug("vinculando channels - ch1 {}, ch2 {} ", event.getCallerId(), se2.getCallerId());
				}
				
				String e = event.getBridgedChannel();
				AbstractCallAsteriskImpl call = new TwoPartyCallImpl(event.getUniqueId(), se2.getCallerId(), event.getCallerId());
				addCall(call);
			} else {
				// no estaba, guardo el evento actual
				channels.put(event.getUniqueId(), event);
			}
		} else {
			AbstractCallAsteriskImpl call = new SinglePartyCallImpl(event.getUniqueId(), callerId, ext);
			addCall(call);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("chanel list - {} ", event.getUniqueId());
		}
	}	
	
	
	// ~~ Gestión de extensiones ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	

	/**
	 * Evento que notifica el cambio de estado de una extension.
	 */
	@Override
	public void handleEvent(ExtensionStatusEvent event) {
		String exten = event.getExten();
		Status status = Status.fromInt(event.getStatus());
		String type = sipList.contains(event.getExten()) ? "SIP" : "IAX";
		
		if (logger.isDebugEnabled()) {
			logger.debug("La extension {} cambió al estado {}", exten, status.toString());
		}
		
		for (EventListener listener : listeners) {
			Extension ext = new ExtensionAsteriskImpl(event.getExten(), status, type);
			listener.extensionChanged(ext);
		}
	}
	
	@Override
	public void handleEvent(PeerlistCompleteEvent event) {
		if (count.incrementAndGet() == 2) {
			
			es.execute(new Runnable() {
				@Override
				public void run() {
					processPeers(sipList, aixList);
				}
			});
			
		}
	}
	
	@Override
	public void handleEvent(PeerEntryEvent event) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Nueva extension (lista) - nro {}", event.getObjectName());
		}
		
		if ("sip".equalsIgnoreCase(event.getChannelType())) {	
			sipList.add(event.getObjectName());
		} else {
			aixList.add(event.getObjectName());
		}
	}
	
	private void processPeers(List<String> sipList, List<String> iaxPeers) {
		for(String sip : sipList) {
			getStatus(sip, "SIP");
		}
		for(String iax : aixList) {
			getStatus(iax, "AIX");
		}
	}


	private void getStatus(String extension, String type) {
		ExtensionStateAction accion = new ExtensionStateAction();
		accion.setContext("from-internal"); // FIXME sacar de la configuracion.
		accion.setExten(extension);

		ExtensionStateResponse respuesta = (ExtensionStateResponse) sendAction(accion);
		if (respuesta != null) {
			int s = respuesta.getStatus();

			Status s1 = Status.fromInt(s);
			if (!Status.INVALID.equals(s1)) {
				for (EventListener listener : listeners) {
					Extension ext = new ExtensionAsteriskImpl(extension, s1, type);
					listener.extensionAdded(ext);
				}
			}
		}
	}
	
	// ~~ Gestión de colas ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Evento que permite listar las colas. Se llama una vez
	 * por cada cola de la central. 
	 */
	@Override
	public void handleEvent(QueueParamsEvent event) {
		if (logger.isDebugEnabled()) {
			logger.debug("Nueva cola (lista), cola {}", event.getQueue());
		}
		for (EventListener listener : listeners) {
			listener.queueAdded(new QueueAsteriskImpl(event.getQueue()));
		}
	}
	
	@Override
	protected void handleEvent(QueueEvent event) {
		if (event instanceof JoinEvent) {
			this.handleEvent((JoinEvent)event);
		} else if (event instanceof LeaveEvent) {
			this.handleEvent((LeaveEvent)event);
		}
	}


	/**
	 * Evento que notifica que se puso una llamada en cola.
	 */
	@Override
	@SuppressWarnings("all")
	protected void handleEvent(JoinEvent event) {
		String queueNumber = event.getQueue();
		String callerId = event.getCallerId();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Llamada ingresando en cola - cola: {}, nro {}", queueNumber, callerId);
		}
		
		queueEntries.put(event.getUniqueId(), callerId);
		for(EventListener listener : listeners) {
			QEntry entry = new QEntryAsteriskImpl(event.getUniqueId(), queueNumber, callerId, new Date());
			listener.queueEntryAdded(event.getQueue(), entry);
		}
	}

	/**
	 * Evento que notifica que una llamada salió de la cola.
	 */
	@Override
	protected void handleEvent(LeaveEvent event) {
		String queueNumber = event.getQueue();
		String callerId = queueEntries.get(event.getUniqueId());

		if (logger.isDebugEnabled()) {
			logger.debug("Llamada abandonando la cola - cola: {}, nro {}", queueNumber, callerId);
		}

		for(EventListener listener : listeners) {
			QEntry entry = new QEntryAsteriskImpl(event.getUniqueId(), event.getQueue(), callerId, new Date());
			listener.queueEntryRemoved(event.getQueue(), entry);
		}
		queueEntries.remove(event.getUniqueId());
	}

	/**
	 * Evento que permite listar los miembros de una cola.
	 * Se llama una vez por cada miembro de cada cola. 
	 */
	@Override
	public void handleEvent(QueueMemberEvent event) {
		Pattern pattern = Pattern.compile(".*/(.*)@.*");
		Matcher matcher = pattern.matcher(event.getName());
		boolean matchFound = matcher.find();
		String memberName = matchFound ? matcher.group(1) : event.getName();
		String queueNumber = event.getQueue();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Miembro logueado en cola - cola: {}, member {}", queueNumber, memberName);
		}
		
		for (EventListener listener : listeners) {
			listener.queueMemberAdded(event.getQueue(), new QMemberAsteriskImpl(memberName, event.getQueue()));
		}
	}
	
	/**
	 * Evento que permite listar las llamadas de una cola.
	 * Se llama una vez por cada llamada que está en una cola.
	 */
	@Override
	public void handleEvent(QueueEntryEvent event) {
		queueEntries.put(event.getUniqueId(), event.getCallerId());
		String queue = event.getQueue();
		String callerId = event.getCallerIdName();

		Calendar gc = new GregorianCalendar();
		gc.add(Calendar.SECOND, -event.getWait().intValue());

		if (logger.isDebugEnabled()) {
			logger.debug("LLamada en cola (lista) - cola: {}, nro {}", queue, callerId);
		}

		for (EventListener listener : listeners) {
			listener.queueEntryAdded(queue, new QEntryAsteriskImpl(event.getUniqueId(), queue, callerId, gc.getTime()));
		}
	}
	
	/**
	 * Evento que notifica que se agregó un miembro en una cola.
	 */
	@Override
	public void handleEvent(QueueMemberAddedEvent event) {
		
		String queue = event.getQueue();
		String memberName = event.getMemberName();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Miembro agregado en cola - cola: {}, member {}", queue, memberName);
		}
		
		for (EventListener listener : listeners) {
			listener.queueMemberAdded(event.getQueue(), new QMemberAsteriskImpl(memberName, queue));
		}
	}
	
	/**
	 * Evento que notifica que se eliminó un miembro en una cola.
	 */
	@Override
	public void handleEvent(QueueMemberRemovedEvent event) {
		
		String queue = event.getQueue();
		String memberName = event.getMemberName();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Miembro retirado de la cola - cola: {}, member {}", queue, memberName);
		}
		
		for (EventListener listener : listeners) {
			listener.queueMemberRemoved(event.getQueue(), new QMemberAsteriskImpl(event.getMemberName(), event.getQueue()));
		}
	}	
	
	// ~~ Acciones de la interface EventListener ~~~~~~~~~~~~~~~~~~~~
	
	@Override
	public void hangup(Call call) {
		final HangupAction ha = new HangupAction(call.getId());
		es.execute(new Runnable() {
			@Override
			public void run() {
				sendAction(ha);
			}
		});		
	}
	
	@Override
	public void record(Call call) {
		if ( call instanceof SinglePartyCallImpl ) {
			return;
		}

		// FIXME, sacar directorio de la configuracion.
		final MonitorAction monitor = new MonitorAction(call.getId(), "/var/spool/asterisk/monitor/custom-"+call.getId(), "wav", true);
		es.execute(new Runnable() {
			@Override
			public void run() {
				sendAction(monitor);
			}
		});
	}
	
	@Override
	public void addCall(AbstractCallAsteriskImpl call) {
		calls.add(call);
		for(EventListener listener : listeners) {
			listener.newCall(call);
		}
	}

	@Override
	public void removeCall(AbstractCallAsteriskImpl call) {
		calls.remove(call);
		for(EventListener listener : listeners) {
			listener.hangupCall(call);
		}
	}
	
	@Override
	public void addCreator(CallCreator creator) {
		creators.add(creator);
	}
	
	@Override
	public void removeCreator(CallCreator creator) {
		creators.remove(creator);
	}
	
	@Override
	public void changeCall(AbstractCallAsteriskImpl call) {
		for(EventListener listener : listeners) {
			listener.changeCall(call);
		}
	}	

	// ~~ Metodos utilitarios ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private ManagerResponse sendAction(ManagerAction action) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Enviando comando {} ", action.getClass().getSimpleName());
		}
		
		try {
			return manager.sendAction(action);
		} catch (IllegalArgumentException e) {
			logger.error("Error al enviar un Action al servidor de asterisk", e);
			return null;
		} catch (IllegalStateException e) {
			logger.error("Error al enviar un Action al servidor de asterisk", e);
			return null;
		} catch (IOException e) {
			logger.error("Error al enviar un Action al servidor de asterisk", e);
			return null;
		} catch (TimeoutException e) {
			logger.error("Error al enviar un Action al servidor de asterisk", e);
			return null;
		}
	}	
}
