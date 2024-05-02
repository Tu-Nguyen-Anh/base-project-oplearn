package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.entity.Addresses;
import org.oplearn.project.exception.base.BadRequestException;
import org.oplearn.project.repository.AddressRepository;
import org.oplearn.project.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
  private final AddressRepository repository;

  @Override
  public AddressResponse create(Addresses addresses) {
    addresses = repository.save(addresses);
    return AddressResponse.of(addresses.getId(), addresses.getProvince(), addresses.getDistrict(), addresses.getWard());
  }

  @Override
  public Addresses create() {
    return repository.save(new Addresses(2, "haha", "421", "21", false));
  }

  @Override
  public AddressResponse get(Integer id) {

    return find(id);
  }

  @Override
  public List<AddressResponse> getAll() {

    return repository.getAll();
  }

  @Override
  public void delete(Integer id) {
    Addresses addresses = repository.findById(id).orElse(null);
    addresses.setDeleted(true);
    repository.save(addresses);
  }

  private AddressResponse find(Integer id) {
    AddressResponse addresses = repository.getById(id);
    if (Objects.isNull(addresses)) {
      throw new BadRequestException();
    }

    return addresses;

  }
}
