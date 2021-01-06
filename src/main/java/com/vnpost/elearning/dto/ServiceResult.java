package com.vnpost.elearning.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author:Nguyen Anh Tuan
 *     <p>May 30,2020
 */
@Getter
@Setter
public class ServiceResult {
  private Object data;
  private String message;
  private Integer totalPage;
  private Long totalItem;
  private Integer currentPage;
  private String code;

  public ServiceResult() {}

  public ServiceResult(Object data, Integer totalPage, Integer currentPage) {
    this.data = data;
    this.totalPage = totalPage;
    this.currentPage = currentPage;
  }

  public ServiceResult(Object data, Long totalItem,String message, String code) {
    this.data = data;
    this.totalItem = totalItem;
    this.message = message;
    this.code = code;
  }

  public ServiceResult(String message, String code) {
    this.message = message;
    this.code = code;
  }


  public ServiceResult(Object data,String message, String code) {
    this.data = data;
    this.message = message;
    this.code = code;
  }
  public ServiceResult(Object data, Long totalItem,Integer currentPage,String message, String code) {
    this.data = data;
    this.totalItem = totalItem;
    this.message = message;
    this.code = code;
    this.currentPage = currentPage;
  }
}
