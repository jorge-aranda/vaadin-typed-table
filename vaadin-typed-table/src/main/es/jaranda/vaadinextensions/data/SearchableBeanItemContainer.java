
package es.jaranda.vaadinextensions.data;

import java.util.Arrays;
import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;

/*
 * Tested for Vaadin 7.3.9-7.4.5 framework
 * Note: This implementation is EXPERIMENTAL until this comment will be
 *       removed
 */

// TODO Complete JAVADOC for this class

public class SearchableBeanItemContainer<BEANTYPE> 
	extends BeanItemContainer<BEANTYPE> {

	private static final long serialVersionUID = -7456743245642657474L;
	
	private Object[] searchableProperties;
	
	private String searchPatrol;
	
	private final Filter searchFilter = new Filter() {
		private static final long serialVersionUID = 1843772354237864338L;

		@Override
		public boolean passesFilter(Object itemId, Item item)
				throws UnsupportedOperationException {
			
			boolean passesFilter = false;
			
			if (getSearchPatrol()!=null && !getSearchPatrol().equals("") 
				&& searchableProperties != null) {
				for (Object searchablePropertyId : searchableProperties) {
					
					Property<?> property =
							item.getItemProperty(searchablePropertyId);
					
					if (property!=null) {
						Object propertyValue = property.getValue();
						
						String propStringValue = propertyValue != null ?
								propertyValue.toString() : "";
						propStringValue = propStringValue.toLowerCase();
						
						if (propStringValue.contains(
								getSearchPatrol().toLowerCase())) {
							passesFilter = true;
						}
					} else {
						passesFilter = true;
					}
				}
			} else {
				passesFilter = true;
			}
			
			return passesFilter;
		}
		
		@Override
		public boolean appliesToProperty(Object propertyId) {
			boolean appliesToProperty;
			
			if (searchableProperties != null) {
				final Collection<Object> colSearchableProperties = 
						Arrays.asList(searchableProperties);
				appliesToProperty =
						colSearchableProperties.contains(propertyId);
				
			} else {
				appliesToProperty = false;
			}
			
			return appliesToProperty;
		}
	};
	
	public SearchableBeanItemContainer(Class<? super BEANTYPE> type)
			throws IllegalArgumentException {
		this(type, (Object[]) null);
	}
	
	public SearchableBeanItemContainer(Class<? super BEANTYPE> type, 
			Object... searchableProperties)
			throws IllegalArgumentException {
		super(type);
		this.setSearchPatrol(null);
		this.setSearchableProperties(searchableProperties);
	}

	public String getSearchPatrol() {
		return searchPatrol;
	}

	public void setSearchPatrol(String searchPatrol) {
		this.searchPatrol = searchPatrol;
		updateSearchFilter();
	}

	public Object[] getSearchableProperties() {
		return searchableProperties;
	}

	public void setSearchableProperties(Object... searchableProperties) {
		this.searchableProperties = searchableProperties;
	}

	private void updateSearchFilter() {
		this.removeContainerFilter(searchFilter);
		this.addContainerFilter(searchFilter);
	}
}
