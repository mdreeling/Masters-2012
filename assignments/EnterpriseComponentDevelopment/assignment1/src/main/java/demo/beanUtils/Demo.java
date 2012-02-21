package demo.beanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

import doconnor.aop.domain.*;
import doconnor.aop.domain.validation.*;

public class Demo {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Demo d = new Demo() ;
		Player p = new Player("Joe Bloggs") ;
		p.setAgent(new Agent()) ;
		d.doStuff(p) ;
	}
	
	public void Demo() { }
	
	private void doStuff(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		PropertyDescriptor[] propertyDescriptors =    
			   PropertyUtils.getPropertyDescriptors(obj.getClass());
			for (PropertyDescriptor propertyDescriptor : 
								propertyDescriptors) {
			    System.out.println( propertyDescriptor.getName() ) ;
			    System.out.println( propertyDescriptor.getPropertyType().getName()) ;

			    Object propertyValue = propertyDescriptor.
							getReadMethod().invoke(obj) ;
			    System.out.println( propertyValue.getClass().getName()) ;
			}

	}
	
}
