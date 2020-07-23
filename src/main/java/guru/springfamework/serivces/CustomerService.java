package guru.springfamework.serivces;

import guru.springfamework.api.v1.model.CustomerDTO;

import java.util.List;


public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerById(Long id);

}
