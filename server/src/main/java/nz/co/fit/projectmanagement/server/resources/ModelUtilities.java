package nz.co.fit.projectmanagement.server.resources;

import static java.beans.Introspector.getBeanInfo;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import nz.co.fit.projectmanagement.server.api.BaseIdable;
import nz.co.fit.projectmanagement.server.dao.entities.BaseIdableModel;
import nz.co.fit.projectmanagement.server.dao.entities.IdableModel;

public class ModelUtilities {

	private static final Map<Class<?>, Map<String, PropertyDescriptor>> propertyMap = new HashMap<>();

	public static <T extends IdableModel> BaseIdable toIdable(final T value) {
		try {
			return convert(value, BaseIdable.class);
		} catch (final ResourceException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <I, O> O convert(final I in, final Class<O> outClass) throws ResourceException {
		Constructor<O> constructor;
		try {
			constructor = outClass.getDeclaredConstructor();
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ResourceException(e);
		}
		constructor.setAccessible(true);
		O out;
		try {
			out = constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new ResourceException(e);
		}
		convert(in, out);
		return out;
	}

	private static <I, O> void convert(final I in, final O out) throws ResourceException {
		final Map<String, PropertyDescriptor> outInfo = getPropertyInfo(out.getClass());
		final Map<String, PropertyDescriptor> inInfo = getPropertyInfo(in.getClass());
		outInfo.entrySet().stream().forEach(e -> {
			final String key = e.getKey();
			final PropertyDescriptor descriptor = e.getValue();
			final Method writeMethod = descriptor.getWriteMethod();
			if (writeMethod == null) {
				return;
			}
			final Class<?> outType = descriptor.getPropertyType();

			final PropertyDescriptor propertyDescriptor = inInfo.get(key);
			if (propertyDescriptor == null) {
				return;
			}
			final Method readMethod = propertyDescriptor.getReadMethod();
			if (readMethod == null) {
				return;
			}
			final Class<?> inType = propertyDescriptor.getPropertyType();

			try {
				final Object propValue = readMethod.invoke(in);

				if (propValue != null && (BaseIdable.class.isAssignableFrom(outType)
						|| BaseIdable.class.isAssignableFrom(inType) || BaseIdableModel.class.isAssignableFrom(inType)
						|| BaseIdableModel.class.isAssignableFrom(outType))) {
					final Object object = convert(propValue, outType);
					writeMethod.invoke(out, object);
				} else {
					writeMethod.invoke(out, propValue);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| ResourceException e1) {
				e1.printStackTrace();
			}
		});
	}

	private static <T> Map<String, PropertyDescriptor> getPropertyInfo(final Class<T> beanClass)
			throws ResourceException {
		if (!propertyMap.containsKey(beanClass)) {
			final HashMap<String, PropertyDescriptor> map = new HashMap<>();
			BeanInfo beanInfo;
			try {
				beanInfo = getBeanInfo(beanClass);
			} catch (final IntrospectionException e) {
				throw new ResourceException(e);
			}

			final PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

			for (final PropertyDescriptor descriptor : descriptors) {
				final String propName = descriptor.getName();
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
