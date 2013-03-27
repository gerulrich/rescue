package net.cloudengine.web.statistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa una serie de datos para representar en un gráfico.
 * @author German Ulrich
 *
 * @param <X> Coordenada X de la serie.
 * @param <Y> Coordenada Y de la serie.
 */
public class GraphSerie<X  extends Number, Y extends Number> {
	
	private String label;
	private List<Number[]> data;
	
	public GraphSerie(String seriesName) {
		super();
		this.label = seriesName;
		this.data = new ArrayList<Number[]>();
	}
	
	void addPoint(X x, Y y) {
		Number point[] = new Number[2];
		point[0] = x;
		point[1] = y;
		data.add(point);
	}
	
	public List<Number[]> getData() {
		List<Number[]> result = new ArrayList<Number[]>();
		result.addAll(data);
		return result;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void fillHols(long diff) {
		Map<Number, Number[]> points = new HashMap<Number, Number[]>();
		if (!data.isEmpty()) {
			for(Number xx[] : data) {
				points.put(xx[0], xx);
			}
			
			Number initPoint[] = data.get(0);
			Number endPoint[] = data.get(data.size()-1);
			for(Long i = initPoint[0].longValue(); i <= endPoint[0].longValue(); i+=diff) {
				if (!points.containsKey(i)) {
					Number newPoint[] = new Number[2];
					newPoint[0] = i;
					newPoint[1] = 0L;
					points.put(i, newPoint);					
				}
			}
			
			List<Number[]> pointList = new ArrayList<Number[]>();
			pointList.addAll(points.values());
			Collections.sort(pointList, new Comparator<Number[]>() {

				@Override
				public int compare(Number[] o1, Number[] o2) {
					Long x1 = o1[0].longValue();
					Long x2 = o2[0].longValue();
					return x1.compareTo(x2);
				}
				
			});
			data = pointList;
			
		}
	}
}
