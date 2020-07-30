package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

public class VendorControllerTest extends AbstractRestControllerTest{

    public static final String VENDOR_NAME = "Dino_Spumoni";


    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getAllVendorsTest() throws Exception {
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName(VENDOR_NAME);
        vendor1.setVendorUrl(VendorController.BASE_URL + "1");

        VendorDTO vendor2 = new VendorDTO();
        vendor2.setName(VENDOR_NAME + "2");
        vendor2.setVendorUrl(VendorController.BASE_URL + "2");

        when(vendorService.getAllVendors()).thenReturn(Arrays.asList(vendor1, vendor2));

        mockMvc.perform(get(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void getVendorById() throws Exception {
        VendorDTO vendor = new VendorDTO();
        vendor.setName("Steve");
        vendor.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.getVendorById(anyLong())).thenReturn(vendor);

        mockMvc.perform(get(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Steve")));
    }

    @Test
    public void createVendor() throws Exception{
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        VendorDTO returnVendorDTO = new VendorDTO();
        returnVendorDTO.setName(VENDOR_NAME);
        returnVendorDTO.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.createVendor(vendorDTO)).thenReturn(returnVendorDTO);

        mockMvc.perform(post(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(jsonPath("$.name", equalTo(VENDOR_NAME)))
                .andExpect(jsonPath("$.vendorUrl", equalTo(VendorController.BASE_URL + "/1")));

    }

    @Test
    public void deleteVendor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService).deleteVendor(anyLong());
    }

    @Test
    public void patchVendor() throws Exception{
        //given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(jsonPath("$.name", equalTo(VENDOR_NAME)))
                .andExpect(jsonPath("$.vendorUrl", equalTo(VendorController.BASE_URL + "/1")));
    }

    @Test
    public void updateVendor() throws Exception {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.saveVendorByDTO(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(jsonPath("$.name", equalTo(VENDOR_NAME)))
                .andExpect(jsonPath("$.vendorUrl", equalTo(VendorController.BASE_URL + "/1")));
    }
}