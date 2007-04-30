/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.jtracc.util;

import org.apache.myfaces.jtracc.util.serial.SerialFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * <p>This Class exposes a handful of methods related to encryption,
 * compression and serialization.</p>
 *
 * <ul>
 * <li>ISO-8859-1 is the character set used.</li>
 * <li>GZIP is used for all compression/decompression.</li>
 * <li>Base64 is used for all encoding and decoding.</li>
 * </ul>
 *
 * <p>To enable encryption, a secret must be provided.  StateUtils looks first
 * for the <i>org.apache.myfaces.SECRET</i> init param.
 * If a secret cannot be located, encryption is not used.</p>
 *
 * <ul>
 * <li>DES is the default encryption algorithm</li>
 * <li>ECB is the default mode</li>
 * <li>PKCS5Padding is the default padding</li>
 * <li>The default algorithm can be overridden using the
 * <i>org.apache.myfaces.ALGORITHM</i> parameter</li>
 * <li>The default mode and padding can be overridden using the
 * <i>org.apache.myfaces.algorithm.PARAMETERS</i> parameter</li>
 * <li>SecretKey cacheing can be disabled using the 
 * <i>org.apache.myfaces.secret.CACHE</i> parameter</li>
 * <li>The defaults are not recommended</li>
 * <li>This utility has not been tested with modes other than ECB and CBC</li>
 * <li>An initialization vector can be specified via the
 * <i>org.apache.myfaces.algorithm.PARAMETERS</i> parameter</li>
 * </ul>
 *
 * <p>All parameters are interpretted as base 64 encoded keys.  In other
 * words, if your secret is "76543210", you would put "NzY1NDMyMTA=" in
 * the deployment descriptor.  This is needed so that key values are not
 * limited to just values composed of printable characters.</p>
 *
 * <p>If you are using CBC mode encryption, you <b>must</b> specify an
 * initialization vector.  StateUtils will throw an exception otherwise.</p>
 *
 * <p>If you are using the AES algorithm and getting a SecurityException
 * complaining about keysize, you most likely need to get the unlimited
 * strength jurisdiction policy files from a place like
 * http://java.sun.com/j2se/1.4.2/download.html .</p>
 *
 * @author Dennis C. Byrne, ich
 * @see org.apache.myfaces.webapp.StartupServletContextListener
 * @noinspection UnusedAssignment
 */

public final class StateUtils {

    private static final Log log = LogFactory.getLog(StateUtils.class);

    public static final String ZIP_CHARSET = "ISO-8859-1";

    public static final String DEFAULT_ALGORITHM = "DES";
    public static final String DEFAULT_ALGORITHM_PARAMS = "ECB/PKCS5Padding";

    public static final String INIT_PREFIX = "org.apache.myfaces.";
    public static final String INIT_SECRET = INIT_PREFIX + "SECRET";
    public static final String INIT_ALGORITHM = INIT_PREFIX + "ALGORITHM";
    public static final String INIT_SECRET_KEY_CACHE = INIT_PREFIX + "secret.CACHE";
    public static final String INIT_ALGORITHM_IV = INIT_PREFIX + "algorithm.IV";
    public static final String INIT_ALGORITHM_PARAM = INIT_PREFIX + "algorithm.PARAMETERS";
    
    public static final String SERIAL_FACTORY = INIT_PREFIX + "SERIAL_FACTORY";
    
    private static final String COMPRESS_STATE_IN_CLIENT = INIT_PREFIX + "COMPRESS_STATE_IN_CLIENT";
    
    /** Utility class, do not instatiate */
    private StateUtils()
    {
    	//nope
    }

    private static void testConfiguration(String algorithmParams, String iv){

        if (algorithmParams != null && algorithmParams.startsWith("CBC") )
        {
        	if(iv == null)
        		throw new FacesException(INIT_ALGORITHM_PARAM +
                                    " parameter has been set with CBC mode," +
                                    " but no initialization vector has been set " +
                                    " with " + INIT_ALGORITHM_IV);
        }

    }
    
    public static boolean enableCompression(ExternalContext ctx)
    {
        if(ctx == null)
            throw new NullPointerException("ExternalContext ctx");
    
        return "true".equals(ctx.getInitParameter(COMPRESS_STATE_IN_CLIENT));
    }
    
    public static boolean isSecure(ExternalContext ctx)
    {
    	
    	if(ctx == null)
    		throw new NullPointerException("ExternalContext ctx");
    	
        return ctx.getInitParameter(INIT_SECRET) != null || ctx.getInitParameter(INIT_SECRET.toLowerCase())!=null;
    }

    /**
     * This fires during the Render Response phase.
     */

    public static String construct(Object object, ExternalContext ctx){
        byte[] bytes = getAsByteArray(object, ctx);
        if( enableCompression(ctx) )
            	bytes = compress(bytes);
        if(isSecure(ctx))
                bytes = encrypt(bytes, ctx);
        bytes = encode(bytes);
        try
        {
            return new String(bytes, ZIP_CHARSET);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new FacesException(e);
        }
    }

    public static byte[] getAsByteArray(Object object, ExternalContext ctx)
    {
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // get the Factory that was instantiated @ startup
        SerialFactory serialFactory = (SerialFactory) ctx.getApplicationMap().get(SERIAL_FACTORY);
        
        if(serialFactory == null)
            throw new NullPointerException("serialFactory");
        
        try
        {
            ObjectOutputStream writer = serialFactory.getObjectOutputStream(outputStream);
            //new ObjectOutputStream(outputStream);
            writer.writeObject(object);
            byte[] bytes = outputStream.toByteArray();
            writer.close();
            outputStream.close();
            writer = null;
            outputStream = null;
            return bytes;
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }
    }

    public static byte[] encrypt(byte[] insecure, ExternalContext ctx)
    {
    	
    	return symmetric(insecure, ctx, Cipher.ENCRYPT_MODE);
    	
    }

    public static byte[] compress(byte[] bytes)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            GZIPOutputStream gzip = new GZIPOutputStream(baos);
            gzip.write(bytes, 0, bytes.length);
            gzip.finish();
            byte[] fewerBytes = baos.toByteArray();
            gzip.close();
            baos.close();
            gzip = null;
            baos = null;
            return fewerBytes;
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }
    }

    public static byte[] encode(byte[] bytes)
    {
    	  return new Base64().encode(bytes);
    }

    /**
     * This fires during the Restore View phase.
     */

    public static Object reconstruct(String string, ExternalContext ctx){
    	byte[] bytes;
        try
        {
            if(log.isDebugEnabled())
                log.debug("Processing state : "+string);            

            bytes = string.getBytes(ZIP_CHARSET);
            bytes = decode(bytes);
            if(isSecure(ctx))
                bytes = decrypt(bytes, ctx);
            if( enableCompression(ctx) )
                bytes = decompress(bytes);
            return getAsObject(bytes, ctx);
        }
        catch (Throwable th)
        {
            throw new FacesException("error while processing state : " + string, th);
        }
    }

    public static byte[] decode(byte[] bytes)
    {
    	  return new Base64().decode(bytes);
    }

    public static byte[] decompress(byte[] bytes)
    {
    	ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	byte[] buffer = new byte[2048];
        int length;

        try
        {
            GZIPInputStream gis = new GZIPInputStream(bais);
            while ((length = gis.read(buffer)) != -1)
            {
                baos.write(buffer, 0, length);
            }

            byte[] moreBytes = baos.toByteArray();
            baos.close();
            bais.close();
            gis.close();
            baos = null;
            bais = null;
            gis = null;
            return moreBytes;
        }
        catch (IOException e)
        {
            throw new FacesException(e);
        }
    }
    
    public static byte[] decrypt(byte[] secure, ExternalContext ctx)
    {
    	
    	return symmetric(secure, ctx, Cipher.DECRYPT_MODE); 

    }

    public static Object getAsObject(byte[] bytes, ExternalContext ctx)
    {
    	ByteArrayInputStream input = new ByteArrayInputStream(bytes);

        // get the Factory that was instantiated @ startup
        SerialFactory serialFactory = (SerialFactory) ctx.getApplicationMap().get(SERIAL_FACTORY);
        
        if(serialFactory == null)
            throw new NullPointerException("serialFactory");
        
    	try
        {
            ObjectInputStream s = serialFactory.getObjectInputStream(input); 
            //new MyFacesObjectInputStream(input);
            Object object = s.readObject();
            s.close();
            input.close();
            s = null;
            input = null;
            return object;
        }
        catch (Exception e)
        {
            throw new FacesException(e);
        }
    }

   public static String encode64(Object obj)
    {
       try
       {
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           OutputStream zos = new GZIPOutputStream(baos);
           ObjectOutputStream oos = new ObjectOutputStream(zos);
           oos.writeObject(obj);
           oos.close();
           zos.close();
           baos.close();
           Base64 base64Codec = new Base64();
           return new String(base64Codec.encode( baos.toByteArray() ), ZIP_CHARSET);
       }
       catch (IOException e)
       {
           log.fatal("Cannot encode Object with Base64", e);
           throw new FacesException(e);
       }
    }

    public static void main (String[] args) throws UnsupportedEncodingException
    {
        byte[] bytes = encode(args[0].getBytes(ZIP_CHARSET));
      	System.out.println(new String(bytes, ZIP_CHARSET));
    }
    
    private static byte[] symmetric(byte[] data, SecretKey secretKey,
            String algorithm, String algorithmParams, byte[] iv, int mode){
    
        try
        {
            // keep local to avoid threading issue
            Cipher cipher = Cipher.getInstance(algorithm + "/"
                    + algorithmParams);
            if (iv != null)
            {
                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                cipher.init(mode, secretKey, ivSpec);
            }
            else
            {
                cipher.init(mode, secretKey);
            }

            if (log.isDebugEnabled())
            {

                String action = mode == Cipher.ENCRYPT_MODE ? "encrypting"
                        : "decrypting";

                log.debug(action + " w/ " + algorithm + "/" + algorithmParams);
            }

            return cipher.doFinal(data);
        }
        catch (Exception e)
        {
            throw new FacesException(e);
        }
    
    }
    
    private static byte[] symmetric(byte[] data, byte[] secret,
            String algorithm, String algorithmParams, byte[] iv, int mode)
    {
        
        if(log.isDebugEnabled())
            log.debug("creating new SecretKey");
        
        SecretKey secretKey = new SecretKeySpec(secret, algorithm);
        return symmetric(data, secretKey, algorithm, algorithmParams, iv, mode);
        
    }

    private static byte[] symmetric(byte[] data, ExternalContext ctx, int mode)
    {

        if (ctx == null)
            throw new NullPointerException("ExternalContext ctx");

        String _secret = ctx.getInitParameter(INIT_SECRET);
        if(_secret == null)
        {
            _secret = ctx.getInitParameter(INIT_SECRET.toLowerCase());
        }
        String _algorithm = ctx.getInitParameter(INIT_ALGORITHM);
        if(_algorithm == null)
        {
            _algorithm = ctx.getInitParameter(INIT_ALGORITHM.toLowerCase());
        }
        String _algorithmParams = ctx.getInitParameter(INIT_ALGORITHM_PARAM);

        if(_algorithmParams == null)
        {
            _algorithmParams = ctx.getInitParameter(INIT_ALGORITHM_PARAM.toLowerCase());
        }

        String _iv = ctx.getInitParameter(INIT_ALGORITHM_IV);

        if(_iv == null)
        {
            _iv = ctx.getInitParameter(INIT_ALGORITHM_IV.toLowerCase());
        }

        String _cache = ctx.getInitParameter(INIT_SECRET_KEY_CACHE);

        if(_cache == null)
        {
            _cache = ctx.getInitParameter(INIT_SECRET_KEY_CACHE.toLowerCase());
        }

        // use isSecure() before calling this method
        if (_secret == null)
            throw new NullPointerException("secret for " + INIT_SECRET
                    + " not located in deployment descriptor");

        if (_algorithm == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Using default algorithm " + DEFAULT_ALGORITHM);
            }
            _algorithm = DEFAULT_ALGORITHM;
        }

        if (_algorithmParams == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Using default algorithm paramaters "
                        + DEFAULT_ALGORITHM_PARAMS);
            }
            _algorithmParams = DEFAULT_ALGORITHM_PARAMS;
        }

        testConfiguration(_algorithmParams, _iv);

        Base64 base64 = new Base64();
        // TODO find a way to avoid decoding each time, maybe context listener

        byte[] iv = null;

        if (_iv != null)
            iv = base64.decode(_iv.getBytes());

        if(_cache != null && "false".equals(_cache)){
            
            // secret will have to be decoded and SecretKey will have to 
            // be generated
            
            byte[] secret = base64.decode(_secret.getBytes());
            
            return symmetric(data, secret, _algorithm, _algorithmParams, iv, mode);
            
        }else{
            
            Object object = ctx.getApplicationMap().get(INIT_SECRET_KEY_CACHE);

            if(object == null)
            {
                object = ctx.getApplicationMap().get(INIT_SECRET_KEY_CACHE.toLowerCase());
            }
            
            if( object == null )
                throw new NullPointerException("The context parameter '" 
                        + INIT_SECRET_KEY_CACHE + "' is not set to false, "
                        + "yet there is nothing stored in the application map "
                        + "w/ the following key '" + INIT_SECRET_KEY_CACHE + "'. "
                        + "It was either not placed there by StartupServletContextListener "
                        + "or something has removed it.");
            
            if( ! ( object instanceof SecretKey ) )
                throw new ClassCastException("Did not find an instance of SecretKey "
                        + "in application scope using the key '" + INIT_SECRET_KEY_CACHE + "'");
            
            if(log.isDebugEnabled())
                log.debug("using cached SecretKey");
            
            return symmetric(data, (SecretKey)object, _algorithm, _algorithmParams, iv, mode);
            
        }
        
    }

    /**
     * Does nothing if the user has disabled the SecretKey cache. This is
     * useful when dealing with a JCA provider whose SecretKey 
     * implementation is not thread safe.
     * 
     * Instantiates a SecretKey instance based upon what the user has 
     * specified in the deployment descriptor.  The SecretKey is then 
     * stored in application scope where it can be used for all requests.
     */
    
    public static void initSecret(ServletContext ctx){
        
        if(ctx == null)
            throw new NullPointerException("ServletContext ctx");
        
        String cache = ctx.getInitParameter(INIT_SECRET_KEY_CACHE);

        if(cache == null)
        {
            cache = ctx.getInitParameter(INIT_SECRET_KEY_CACHE.toLowerCase());
        }
        
        if("false".equals(cache))
            return;
        
        String _secret = ctx.getInitParameter(INIT_SECRET);

        if(_secret == null)
        {
            _secret = ctx.getInitParameter(INIT_SECRET.toLowerCase());
        }

        String _algorithm = ctx.getInitParameter(INIT_ALGORITHM);

        if(_algorithm == null)
        {
            _algorithm = ctx.getInitParameter(INIT_ALGORITHM.toLowerCase());
        }
        
        if (_algorithm == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Using default algorithm " + DEFAULT_ALGORITHM);
            }
            _algorithm = DEFAULT_ALGORITHM;
        }
        
        if(_secret == null)
            throw new NullPointerException("_secret String - '"
                    + INIT_SECRET_KEY_CACHE + "' has been enabled, "
                    + "but there is no '" + INIT_SECRET + "'");
        
        byte[] secret = new Base64().decode(_secret.getBytes());
        
        // you want to do this as few times as possible
        SecretKey secretKey = new SecretKeySpec(secret, _algorithm);
        
        if (log.isDebugEnabled())
            log.debug("Storing SecretKey @ " + INIT_SECRET_KEY_CACHE);
        
        ctx.setAttribute(INIT_SECRET_KEY_CACHE, secretKey);
        
    }
}
