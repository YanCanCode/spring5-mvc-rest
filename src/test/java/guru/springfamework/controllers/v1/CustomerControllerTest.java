package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.services.CustomerService;
import guru.springfamework.services.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends AbstractRestControllerTest {

    public static final String NAME1 = "Steve";
    public static final String NAME2 = "John";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getAllCustomersTest() throws Exception {
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setFirstName(NAME1);
        customer1.setLastName(NAME2);
        customer1.setCustomerUrl(CustomerController.BASE_URL + "/1");

        CustomerDTO customer2 = new CustomerDTO();
        customer2.setFirstName(NAME2);
        customer2.setLastName(NAME1);
        customer2.setCustomerUrl(CustomerController.BASE_URL + "/2");

        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customer1,customer2));

        mockMvc.perform(get(CustomerController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(2)));
    }

    @Test
    public void getCustomerByIdTest() throws Exception {
        //given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName(NAME1);
        customer.setLastName(NAME2);
        customer.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(customerService.getCustomerById(anyLong())).thenReturn(customer);

        //when
        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Steve")));
        }

    @Test
    public void createNewCustomer() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(NAME1);
        customerDTO.setLastName(NAME2);

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customerDTO.getFirstName());
        returnDTO.setLastName(customerDTO.getLastName());
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(customerService.createNewCustomer(customerDTO)).thenReturn(returnDTO);

        mockMvc.perform(post("/api/v1/customers/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo(NAME1)))
                .andExpect(jsonPath("$.lastName", equalTo(NAME2)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL + "/1")));
    }

    @Test
    public void updateCustomerTest() throws Exception {
        //given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(NAME1);
        customerDTO.setLastName(NAME2);

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customerDTO.getFirstName());
        returnDTO.setLastName(customerDTO.getLastName());
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(customerService.saveCustomerByDTO(anyLong(),any(CustomerDTO.class))).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(put("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(NAME1)))
                .andExpect(jsonPath("$.lastName", equalTo(NAME2)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL + "/1")));
    }

    @Test
    public void patchCustomerTest() throws Exception {
        //given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName(NAME1);

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customer.getFirstName());
        returnDTO.setLastName(NAME2);
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(NAME1)))
                .andExpect(jsonPath("$.lastName", equalTo(NAME2)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL + "/1")));
    }

    @Test
    public void deleteByIdTest() throws Exception {
        mockMvc.perform(delete(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService).deleteCustomer(anyLong());
    }


    @Test
    public void testNotFoundException() throws Exception {

        when(customerService.getCustomerById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CustomerController.BASE_URL + "/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
