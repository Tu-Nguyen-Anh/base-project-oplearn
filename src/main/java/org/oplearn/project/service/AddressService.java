package org.oplearn.project.service;

import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.entity.Addresses;

import java.util.List;

public interface AddressService {
  AddressResponse create(Addresses addresses);

  Addresses create();

  AddressResponse get(Integer id);

  List<AddressResponse> getAll();

  void delete(Integer id);
}
