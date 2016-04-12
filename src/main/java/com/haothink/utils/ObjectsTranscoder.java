package com.haothink.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectsTranscoder <T extends Serializable> extends SerializeTranscoder {
	@SuppressWarnings("unchecked")
	@Override
	public byte[] serialize(Object value) {
		// TODO Auto-generated method stub
		if (value == null) {  
			throw new NullPointerException("Can't serialize null");  
		}  
		byte[] result = null;  
		ByteArrayOutputStream bos = null;  
		ObjectOutputStream os = null;  
		try {  
			bos = new ByteArrayOutputStream();  
			os = new ObjectOutputStream(bos);
			T m = (T) value;
			os.writeObject(m);  
			os.close();  
			bos.close();  
			result = bos.toByteArray();  
		} catch (IOException e) {  
			throw new IllegalArgumentException("Non-serializable object", e);  
		} finally {  
			close(os);  
			close(bos);  
		}  
		return result;  
	}

	@SuppressWarnings("unchecked")
	@Override
	public T deserialize(byte[] in) {
		T result = null;  
		ByteArrayInputStream bis = null;  
		ObjectInputStream is = null;  
		try {  
			if (in != null) {  
				bis = new ByteArrayInputStream(in);  
				is = new ObjectInputStream(bis);  
				result = (T) is.readObject();  
				is.close();  
				bis.close();  
			}  
		} catch (IOException e) {  
			logger.error(e);  
		} catch (ClassNotFoundException e) {  
			logger.error(e);  
		} finally {  
			close(is);  
			close(bis);  
		}  
		return result;  
	}
	
	@SuppressWarnings("unchecked")
	  public List<T> deserializeList(byte[] in) {
	    List<T> list = new ArrayList<T>();
	    ByteArrayInputStream bis = null;
	    ObjectInputStream is = null;
	    try {
	      if (in != null) {
	        bis = new ByteArrayInputStream(in);
	        is = new ObjectInputStream(bis);
	        while (true) {
	          T m = (T)is.readObject();
	          if (m == null) {
	            break;
	          }
	          list.add(m);
	        }
	        is.close();
	        bis.close();
	      }
	    } catch (IOException e) {  
	    	logger.error(e);
	  } catch (ClassNotFoundException e) {  
		  logger.error(e);
	  }  finally {
	      close(is);
	      close(bis);
	    }
	    
	    return  list;
	  }
	  

	  @SuppressWarnings("unchecked")
	  public byte[] serializeList(Object value) {
	    if (value == null)
	      throw new NullPointerException("Can't serialize null");
	    List<T> values = (List<T>) value;
	    byte[] results = null;
	    ByteArrayOutputStream bos = null;
	    ObjectOutputStream os = null;
	    try {
	      bos = new ByteArrayOutputStream();
	      os = new ObjectOutputStream(bos);
	      for (T t : values) {
	        os.writeObject(t);
	      }
	      
	      // os.writeObject(null);
	      os.close();
	      bos.close();
	      results = bos.toByteArray();
	    } catch (IOException e) {
	      throw new IllegalArgumentException("Non-serializable object", e);
	    } finally {
	      close(os);
	      close(bos);
	    }  
	    return results;
	  }

}


