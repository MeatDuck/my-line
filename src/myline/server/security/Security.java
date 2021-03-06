package myline.server.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import myline.shared.exceptions.ServiceException;
import myline.shared.security.Access;

public class Security {
	private static final Logger LOG = Logger.getLogger(Security.class.getName());
	
	public boolean isValid(Access acc) throws ServiceException {
		if(acc == null){
			throw new ServiceException("Bad vkontakte creditails, opened not in vkontakte");
		}
		if(acc.getUid() == null || acc.getSign() == null){
			throw new ServiceException("Bad vkontakte creditails, opened not in vkontakte");
		}
		
		LOG.info("[" + acc.getUid().equals(SecurityConstants.NONPAS_USER) + ", " + acc.getSign().equals(SecurityConstants.NONPAS_SECRET_HASH) + "]");
		if(acc.getUid().equals(SecurityConstants.NONPAS_USER) && 
		   acc.getSign().equals(SecurityConstants.NONPAS_SECRET_HASH)
		   ){
			return true;
		}
		if(acc.getUid().equals(SecurityConstants.NONPAS_USER2) && 
		   acc.getSign().equals(SecurityConstants.NONPAS_SECRET_HASH2)
		   ){
					return true;
				}
		//auth_key = md5(api_id + '_' + viewer_id + '_' + api_secret)
		LOG.info("try to validate user = " + acc.getUid() + " with sign " + acc.getSign());
		
		final String createMD5 = createMD5(SecurityConstants.API_ID + "_" + acc.getUid() + "_" + SecurityConstants.API_SECRET);
		LOG.info("md5 = " + createMD5);
		if(createMD5.equals(acc.getSign())){
			return true;
		}
		throw new ServiceException("Bad vkontakte creditails");
	}
	
	public String createMD5(String raw)
	   {
		  BigInteger output = null;
	       try
	       {
	           MessageDigest messageDigest = MessageDigest.getInstance("MD5");
	           messageDigest.update(raw.getBytes(), 0, raw.length());
	           output = new BigInteger(1, messageDigest.digest());
	       }
	       catch (NoSuchAlgorithmException e)
	       {
	    	   LOG.info(e.getMessage());
	       }
	       return String.format("%1$032x", output);
	   }

	
}
