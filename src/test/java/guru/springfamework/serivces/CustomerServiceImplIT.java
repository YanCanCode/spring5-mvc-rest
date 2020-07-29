package guru.springfamework.serivces;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.bootstrap.Bootstrap;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerServiceImplIT {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    CustomerServiceImpl customerService;

    @Before
    public void setUp() throws Exception {
        System.out.println("Loading customer data: ");
        System.out.println(customerRepository.findAll().size());

        //load data to test against
        Bootstrap bootstrap = new Bootstrap(categoryRepository,customerRepository);
        bootstrap.run();

        customerService = new CustomerServiceImpl();
        customerService.setCustomerMapper(CustomerMapper.INSTANCE);
        customerService.setCustomerRepository(customerRepository);

    }

    @Test
    public void patchCustomerUpdateFirstName() throws Exception {
        String updatedName = "UpdatedName";
        long id = getCustomerIdValue();

        Customer orginalCustomer = customerRepository.getOne(id);
        assertNotNull(orginalCustomer);

        //save original first name
        String originalFirstName = orginalCustomer.getFirstName();
        String originalLastName = orginalCustomer.getLastName();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(updatedName);

        customerService.patchCustomer(id, customerDTO);

        Customer updatedCustomer = customerRepository.findById(id).get();

        assertNotNull(updatedCustomer);
        assertEquals(updatedName, updatedCustomer.getFirstName());
        assertThat(originalFirstName, not(equalTo(updatedCustomer.getFirstName())));
        assertThat(orginalCustomer.getLastName(), equalTo(updatedCustomer.getLastName()));
    }

    @Test
    public void patchCustomerUpdateLastName() throws Exception {
        String updatedName = "UpdatedName";
        long id = getCustomerIdValue();

        Customer orginalCustomer = customerRepository.getOne(id);
        assertNotNull(orginalCustomer);

        //save original last name
        String originalFirstName = orginalCustomer.getFirstName();
        String originalLastName = orginalCustomer.getLastName();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName(updatedName);

        customerService.patchCustomer(id, customerDTO);

        Customer updatedCustomer = customerRepository.findById(id).get();

        assertNotNull(updatedCustomer);
        assertEquals(updatedName, updatedCustomer.getLastName());
        assertThat(originalFirstName, equalTo(updatedCustomer.getFirstName()));
        assertThat(originalLastName, not(equalTo(updatedCustomer.getLastName())));
    }

    public long getCustomerIdValue(){
        List<Customer> customers = customerRepository.findAll();

        System.out.println("Customers found: " + customers.size());

        //return the first customers id
        return customers.get(0).getId();
    }
}
