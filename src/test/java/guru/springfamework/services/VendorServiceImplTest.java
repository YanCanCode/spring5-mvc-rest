package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class VendorServiceImplTest {

    @Mock
    VendorRepository vendorRepository;

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    VendorServiceImpl vendorService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vendorService = new VendorServiceImpl(vendorMapper, vendorRepository);
    }

    @Test
    public void getAllVendors() {
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        vendor1.setName("Larry's Loopy Crazy");

        Vendor vendor2 = new Vendor();
        vendor2.setId(2L);
        vendor2.setName("SevenEleven");

        when(vendorRepository.findAll()).thenReturn(Arrays.asList(vendor1, vendor2));

        //when
        List<VendorDTO> vendorDTOS = vendorService.getAllVendors();

        //then
        assertEquals(2, vendorDTOS.size());
    }

    @Test
    public void getVendorById() {
        //given
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        vendor1.setName("Steve");

        when(vendorRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(vendor1));

        //when
        VendorDTO vendorDTO = vendorService.getVendorById(1L);

        //then
        assertEquals("Steve", vendorDTO.getName());

    }

    @Test
    public void createVendor() {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Hot Hot Heat");

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(1L);

        when(vendorRepository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(savedVendor);

        //when
        VendorDTO saveDTO = vendorService.createVendor(vendorDTO);

        //then
        assertEquals(vendorDTO.getName(), saveDTO.getName());
        assertEquals(VendorController.BASE_URL + "/1", saveDTO.getVendorUrl());
    }

    @Test
    public void deleteVendor() {
        Long id = 1L;

        vendorRepository.deleteById(id);

        verify(vendorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void saveVendorByDTO() {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Hot Hot Heat");

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(1L);

        when(vendorRepository.save(ArgumentMatchers.any(Vendor.class))).thenReturn(savedVendor);

        //when
        VendorDTO saveDTO = vendorService.saveVendorByDTO(1L, vendorDTO);

        //then
        assertEquals(vendorDTO.getName(), saveDTO.getName());
        assertEquals(VendorController.BASE_URL + "/1", saveDTO.getVendorUrl());
    }
}