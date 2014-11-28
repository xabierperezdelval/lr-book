/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.inikah.slayer.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;

import com.inikah.slayer.model.Location;
import com.inikah.slayer.service.base.LocationLocalServiceBaseImpl;
import com.inikah.util.IConstants;
import com.inikah.util.NotifyUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Address;
import com.liferay.portal.model.Country;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ClassNameLocalServiceUtil;
import com.liferay.portal.service.CountryServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * The implementation of the location local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.inikah.slayer.service.LocationLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ahmed Hasan
 * @see com.inikah.slayer.service.base.LocationLocalServiceBaseImpl
 * @see com.inikah.slayer.service.LocationLocalServiceUtil
 */
public class LocationLocalServiceImpl extends LocationLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.inikah.slayer.service.LocationLocalServiceUtil} to access the location local service.
	 */
	
	public Location getLocation(long parentId, String name, int locType, long userId) {
		Location location = null;
		try {
			location = locationPersistence.fetchByParentId_Name(parentId, name, locType);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (!location.isActive_()) {
			location.setActive_(true);
			try {
				location = updateLocation(location);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		if (Validator.isNotNull(location)) return location;
		
		long locationId = 0l;
		try {
			locationId = counterLocalService.increment(Location.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		location = createLocation(locationId);
		location.setParentId(parentId);
		location.setName(name);
		location.setLocType(locType);
		location.setUserId(userId);
		location.setActive_(true);
		
		try {
			location = updateLocation(location);
			NotifyUtil.newCityCreated(location);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return location;		
	}
	
	public Location getLocation(long parentId, String code, String name, long userId) {
		
		Location location = null;
		try {
			location = locationPersistence.fetchByParentId_Code(parentId, code, IConstants.LOC_TYPE_REGION);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(location)) return location;
		
		long locationId = 0l;
		try {
			locationId = counterLocalService.increment(Location.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		location = locationPersistence.create(locationId);
		
		location.setParentId(parentId);
		location.setCode(code);
		location.setLocType(IConstants.LOC_TYPE_REGION);
		location.setUserId(userId);
		location.setName(StringUtil.replace(name, "State Of ", StringPool.BLANK));
		location.setActive_(true);
		
		try {
			location = updateLocation(location);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return location;
	}
	
	public List<Location> getLocations(long parentId, int locType) {
		List<Location> locations = null;
		try {
			locations = locationPersistence.findByParentId_LocType_Active(parentId, locType, true);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return locations;
	}
	
	public List<Location> getLocations(long parentId, int locType, boolean all) {
		List<Location> locations = null;
		try {
			locations = locationPersistence.findByParentId_LocType(parentId, locType);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return locations;
	}	
	
	public String getDisplayInfo(long cityId) {
		StringBuilder sb = new StringBuilder();

		try {
			Location location = locationPersistence.fetchByPrimaryKey(cityId);
			sb.append(location.getName()).append(StringPool.COMMA).append(StringPool.SPACE);
			
			location = locationPersistence.fetchByPrimaryKey(location.getParentId());
			sb.append(location.getName()).append(StringPool.COMMA).append(StringPool.SPACE);
			
			Country country = CountryServiceUtil.fetchCountry(location.getParentId());
			sb.append(country.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	public String getUserLocation(long userId) {
		String location = StringPool.BLANK;
		try {
			User user = UserLocalServiceUtil.fetchUser(userId);
			
			Address address = getLocation(user);
			
			if (Validator.isNotNull(address)) {
				location = getDisplayInfo(Long.valueOf(address.getCity()));
			}			
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return location;
	}
	
	public boolean userHasLocation(long userId) {
		return Validator.isNotNull(getUserLocation(userId));
	}
	
	public long insertCity(long regionId, String name, long userId) {
		
		long cityId = 0l;
		
		List<Location> cities = null;
		try {
			cities = locationPersistence.findByParentId_LocType(regionId, IConstants.LOC_TYPE_CITY);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		// check for soundex
		for (Location city: cities) {
			int diff = 0;
			try {
				diff = Soundex.US_ENGLISH.difference(city.getName(), name);				
			} catch (EncoderException e) {
				e.printStackTrace();
			}
			
			if (diff == 4) {
				cityId = city.getLocationId();
				break;
			}
		}
		
		if (cityId == 0l) {
			try {
				cityId = counterLocalService.increment(Location.class.getName());
			} catch (SystemException e) {
				e.printStackTrace();
			}
			Location city = createLocation(cityId);
			city.setName(name);
			city.setUserId(userId);
			city.setLocType(IConstants.LOC_TYPE_CITY);
			city.setParentId(regionId);
			city.setActive_(false);
			
			try {
				addLocation(city);
				NotifyUtil.newCityCreated(city);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}	
		return cityId;
	}
	
	/**
	 * 
	 */
	public Address getLocation(User user) {
		
		Address address = null;
		try {
			List<Address> addresses = 
					addressLocalService.getAddresses(
							user.getCompanyId(), Location.class.getName(), user.getUserId());
			
			if (Validator.isNotNull(addresses) && !addresses.isEmpty()) {
				address = addresses.get(addresses.size()-1);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return address;
	}
	
	/**
	 * 
	 */
	public boolean isLocationSet(User user) {
		
		long userId = user.getUserId();
		boolean locationSet = false;
		String ipAddress = user.getLastLoginIP();
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				Address.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("zip", ipAddress));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classNameId",
				ClassNameLocalServiceUtil.getClassNameId(Location.class)));
		
		Address existing = null;
		try {
			@SuppressWarnings("unchecked")
			List<Address> addresses = addressLocalService.dynamicQuery(dynamicQuery);
			
			for (Address address: addresses) {
				existing = address;
				if (address.getClassPK() == userId) {
					locationSet = true;
					break;
				}
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (!locationSet && Validator.isNotNull(existing)) {
			existing = insertAddress(userId, existing.getStreet1(), existing.getStreet2(),
					existing.getStreet3(), Long.valueOf(existing.getCity()),
					existing.getRegionId(), existing.getCountryId(), ipAddress);
			
			existing.setUserName(user.getFullName());
			
			try {
				addressLocalService.updateAddress(existing);
			} catch (SystemException e) {
				e.printStackTrace();
			}
			
			locationSet = true;
		}
		
		return locationSet;
	}
	
	public Address insertAddress(long userId, String street1, String street2,
			String street3, long cityId, long regionId,
			long countryId, String ipAddress) {
		
		Address address = null;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				Address.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classNameId",
				ClassNameLocalServiceUtil.getClassNameId(Location.class)));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classPK", userId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("city", String.valueOf(cityId)));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("regionId", regionId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("countryId", countryId));
		
		try {
			@SuppressWarnings("unchecked")
			List<Address> addresses = addressLocalService.dynamicQuery(dynamicQuery);
			for (Address _address: addresses) {
				address = _address;
				break;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(address)) return address;
		
		try {
			ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
			address = addressLocalService.addAddress(userId, Location.class.getName(), userId, 
					street1, street2, street3, String.valueOf(cityId), ipAddress, regionId, countryId, 
					IConstants.PHONE_VERIFIED, false, true, serviceContext);
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}					
		
		return address;
	}
	
	public Location setCoordinates(User user) {
		
		String ipAddress = user.getLastLoginIP();
		
		Location location = null;
		
		if (existingIPAddress(ipAddress)) {
			location = getLocationForIP(ipAddress);
		} else {
			
			String country = "IN";
			String region = "Karnataka";
			String city = "Bangalore";
			
			if (Validator.isIPAddress(ipAddress)) {
				String url = "http://ipinfo.io/" + ipAddress + "/geo";
				
				HttpClient client = new HttpClient();
				GetMethod method = new GetMethod(url);
				
				try {
					client.executeMethod(method);
				} catch (HttpException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				InputStream inputStream = null;
				try {
					inputStream = method.getResponseBodyAsStream();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				String jsonResponse = null;
				try {
					jsonResponse = IOUtils.toString(inputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				JSONObject jsonObject = null;
				try {
					jsonObject = JSONFactoryUtil.createJSONObject(jsonResponse);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				country = jsonObject.getString("country");
				region = jsonObject.getString("region");
				city = jsonObject.getString("city");
			}
			
			location = locationFinder.getCity(country, region, city);
			
			if (Validator.isNull(location)) {
				location = insertCity(country, region, city);
			}			
		}
		
		updateAddress(user, location);
		
		return location;
	}

	private Location getLocationForIP(String ipAddress) {
		// TODO Auto-generated method stub
		
		Location location = null;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				Address.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("zip", ipAddress));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classNameId",
				ClassNameLocalServiceUtil.getClassNameId(Location.class)));
		
		try {
			@SuppressWarnings("unchecked")
			List<Address> addresses = addressLocalService.dynamicQuery(dynamicQuery);
			
			if (Validator.isNotNull(addresses) && addresses.size() > 0) {
				Address address = addresses.get(0);
				
				long countryId = address.getCountryId();
				long regionId = address.getRegionId();
				long cityId = Long.valueOf(address.getCity());
				
				location = locationFinder.getCity(countryId, regionId, cityId);
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return location;
	}

	private boolean existingIPAddress(String ipAddress) {
		
		if (Validator.isNull(ipAddress) || ipAddress.equalsIgnoreCase("127.0.0.1")) {
			return false;
		}
		
		boolean existing = false;
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				Address.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("zip", ipAddress));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classNameId",
				ClassNameLocalServiceUtil.getClassNameId(Location.class)));
		
		try {
			@SuppressWarnings("unchecked")
			List<Address> addresses = addressLocalService.dynamicQuery(dynamicQuery);
			existing = (Validator.isNotNull(addresses) && addresses.size() > 0);
		} catch (SystemException e) {
			e.printStackTrace();
		}		
		
		return existing;
	}

	private void updateAddress(User user, Location location) {

		// check if a record exists in the 'Address' table with the same coordinates
		
		long cityId = location.getLocationId();
		long regionId = location.getParentId();
		long countryId = 0l;
		
		try {
			Location _region = fetchLocation(regionId);
			countryId = _region.getParentId();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		long userId = user.getUserId();
		
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
				Address.class, PortalClassLoaderUtil.getClassLoader());
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classNameId",
				ClassNameLocalServiceUtil.getClassNameId(Location.class)));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("classPK", userId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("city", String.valueOf(cityId)));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("regionId", regionId));
		dynamicQuery.add(RestrictionsFactoryUtil.eq("countryId", countryId));
		
		Address address = null;
		
		try {
			@SuppressWarnings("unchecked")
			List<Address> addresses = addressLocalService.dynamicQuery(dynamicQuery);
			for (Address _address: addresses) {
				address = _address;
				break;
			}
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNull(address)) {
			try {
				ServiceContext serviceContext = ServiceContextThreadLocal.getServiceContext();
				address = addressLocalService.addAddress(userId, Location.class.getName(), userId, 
						StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, String.valueOf(cityId), user.getLastLoginIP(), regionId, countryId, 
						IConstants.PHONE_VERIFIED, false, true, serviceContext);
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			}	
		}
	}

	private Location insertCity(String country, String region, String city) {

		Location _region = locationFinder.getRegion(country, region);
		
		if (Validator.isNull(_region)) {
			_region = insertRegion(country, region);
		}
		
		long locationId = 0l;
		try {
			locationId = counterLocalService.increment(Location.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}		
		Location _city = createLocation(locationId);
		_city.setParentId(_region.getLocationId());
		_city.setName(city);
		_city.setActive_(true);
		_city.setLocType(IConstants.LOC_TYPE_CITY);		
		
		try {
			_city = addLocation(_city);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		 
		return _city;
	}

	private Location insertRegion(String country, String region) {
		
		long locationId = 0l;
		try {
			locationId = counterLocalService.increment(Location.class.getName());
		} catch (SystemException e) {
			e.printStackTrace();
		}

		Location _region = createLocation(locationId);
		
		Country _country = null;
		try {
			_country = CountryServiceUtil.fetchCountryByA2(country);
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		if (Validator.isNotNull(_country)) {
			_region.setParentId(_country.getCountryId());
			_region.setName(region);
			_region.setActive_(true);
			_region.setLocType(IConstants.LOC_TYPE_REGION);
			
			try {
				_region = addLocation(_region);
			} catch (SystemException e) {
				e.printStackTrace();
			}
		}
		
		return _region;
	}
}