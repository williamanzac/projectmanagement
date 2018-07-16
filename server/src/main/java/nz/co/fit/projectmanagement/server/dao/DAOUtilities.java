package nz.co.fit.projectmanagement.server.dao;

import static java.beans.Introspector.getBeanInfo;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.greenrobot.eventbus.EventBus;

import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

public class DAOUtilities {

	private static final Map<Class<?>, Map<String, PropertyDescriptor>> propertyMap = new HashMap<>();

	/**
	 * Update the fields of the existing model object with the values from the fields of the new value object.
	 *
	 * @param existing
	 *            the existing DB model object
	 * @param value
	 *            the incoming model object
	 * @param excluding
	 *            a list of field names to exclude
	 * @throws DAOException
	 */
	public static <T extends IdableModel> void updateAndNotify(final T existing, final T value,
			final List<String> excluding) throws DAOException {
		final Map<String, PropertyDescriptor> info = getPropertyInfo(existing.getClass());
		info.entrySet().forEach(e -> {
			final String fieldName = e.getKey();
			if (excluding.contains(fieldName)) {
				// if the field name is in the exclude list then skip it.
				return;
			}

			// get the property descriptor and the getter method
			final PropertyDescriptor descriptor = e.getValue();
			final Method readMethod = descriptor.getReadMethod();
			if (readMethod == null) {
				// if there is no getter method then skip, as we will not be able to read the existing value
				return;
			}

			try {
				// get the old and new values for the field
				final Object existingPropValue = readMethod.invoke(existing);
				final Object valuePropValue = readMethod.invoke(value);
				if (!Objects.equals(existingPropValue, valuePropValue)) {
					// the old and new values are different, so we will need to record the change
					final String existingString;
					if (existingPropValue instanceof IdableModel) {
						// the field is another model object so just use the id field
						existingString = String.valueOf(((IdableModel) existingPropValue).getId());
					} else {
						// get the toString value of the object
						existingString = String.valueOf(existingPropValue);
					}
					final String valueString;
					if (valuePropValue instanceof IdableModel) {
						// the field is another model object so just use the id field
						valueString = String.valueOf(((IdableModel) valuePropValue).getId());
					} else {
						// get the toString value of the object
						valueString = String.valueOf(valuePropValue);
					}

					// post the event to the bus
					EventBus.getDefault().post(new UpdateEvent(fieldName, existingString, valueString, existing));

					// get the setter method
					final Method writeMethod = descriptor.getWriteMethod();
					if (writeMethod == null) {
						// if there is not a setter method, then we cannot update the existing value
						return;
					}
					// set the new field value
					writeMethod.invoke(existing, valuePropValue);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
			}
		});
	}

	public static <T extends IdableModel> void deleteHistory(final T value) throws DAOException {
		EventBus.getDefault().post(new DeleteEvent(value));
	}

	/**
	 * Get the Bean property descriptor for the specified class, first time through it should populate a cache, so that
	 * on additional calls it can get it from the cache.
	 *
	 * @param beanClass
	 *            the class to get the descriptors for
	 * @return a map of field names to descriptor
	 * @throws DAOException
	 */
	private static <T> Map<String, PropertyDescriptor> getPropertyInfo(final Class<T> beanClass) throws DAOException {
		// check if the class is already in the cache
		if (!propertyMap.containsKey(beanClass)) {
			final HashMap<String, PropertyDescriptor> map = new HashMap<>();
			BeanInfo beanInfo;
			try {
				// get the bean info for the class
				beanInfo = getBeanInfo(beanClass);
			} catch (final IntrospectionException e) {
				throw new DAOException(e);
			}

			final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

			for (final PropertyDescriptor descriptor : descriptors) {
				final String propName = descriptor.getName();
				// check if the property name is blank or 'class'
				if (isBlank(propName) || "class".equals(propName)) {
					continue;
				}
				map.put(propName, descriptor);
			}
			propertyMap.put(beanClass, map);
		}
		return propertyMap.get(beanClass);
	}
}
