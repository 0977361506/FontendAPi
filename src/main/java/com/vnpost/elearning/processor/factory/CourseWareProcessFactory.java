package com.vnpost.elearning.processor.factory;

import com.vnpost.elearning.processor.ICWProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:Nguyen Anh Tuan
 *     <p>September 16,2020
 */
@Service
public class CourseWareProcessFactory {
  private static final Map<String, ICWProcess> myServiceCache = new HashMap<>();

  @Autowired
  public CourseWareProcessFactory(List<ICWProcess> list) {
    for (ICWProcess icwProcess : list) {
      myServiceCache.put(icwProcess.getType(), icwProcess);
    }
  }

  public static ICWProcess getProcess(String type) {
    ICWProcess process = myServiceCache.get(type);
    if (process == null) throw new RuntimeException("Unknown services type: " + type);
    return myServiceCache.get(type);
  }
}
