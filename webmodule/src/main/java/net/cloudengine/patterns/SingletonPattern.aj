package net.cloudengine.singleton;

import java.util.Hashtable;
import net.cloudengine.patterns.Singleton;

public aspect SingletonPattern issingleton( ) {
	
   private Hashtable singletons = new Hashtable( );

   // Pointcut to define specify an interest in all creations
   // of all Classes that extend Singleton
   
   pointcut selectSingletons( ) : execution((Singleton +).new ());

   // Pointcut to ensure that any classes in the Singleton inheritance tree
   // that are marked as Non Singletons are not included in the Singleton
   // logic.
   
   //pointcut excludeNonSingletons( ) : !execution((NonSingleton +).new (..));

   private Singleton Singleton._instance;
//   private static boolean  Singleton._init = false;
   
   Object around( ) : selectSingletons( ) {
	   

	   Singleton target = (Singleton) thisJoinPoint.getTarget();
	   Class singleton = thisJoinPoint.getSignature( ).getDeclaringType( );
	   
	   if (singletons.get(singleton) == null) {
		   singletons.put(singleton, new Object());
//		   System.out.println("ya inicie el singleton");
		   try {
			   target._instance = (Singleton) proceed();
			   System.out.println(target._instance);
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		   System.out.println("ya inicie el singleton de: "+singleton);
		   singletons.put(singleton, target._instance);
	   } else {
		   System.out.println("no hago uno nuevo");
	   }
	   System.out.println(singletons.get(singleton));
	   return (Object) singletons.get(singleton);
//	   return null;
//	   
//	   System.out.println("estoy en un aspecto");
//	   
//	   
//	   
//	   synchronized(singletons) {
//         if (singletons.get(singleton) == null) {
//             try {
//            	 singletons.put(singleton,  proceed());
//             } catch (Exception e) {
//            	 
//             }
//        	 
//        	 
//         }
//      }
//      System.out.println( "hola: "+singletons.get(singleton) );
//      return (Object) singletons.get(singleton);
   }
   
   
   
   
}