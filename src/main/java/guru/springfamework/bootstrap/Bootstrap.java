package guru.springfamework.bootstrap;

import guru.springfamework.domain.Category;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        loadCategories();
        loadCustomers();
        loadVendors();
    }

    private void loadCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("Suzy");
        customer1.setLastName("Swanson");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Benny");
        customer2.setLastName("Benoit");

        Customer customer3 = new Customer();
        customer3.setId(3L);
        customer3.setFirstName("Larold");
        customer3.setLastName("Barold");

        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);

        System.out.println("Customers loaded: " + customerRepository.count());
    }

    private void loadCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);

        System.out.println("Data loaded = " + categoryRepository.count());
    }

    private void loadVendors(){
        Vendor tasty = new Vendor();
        tasty.setName("Western Tasty Fruits Ltd.");
        vendorRepository.save(tasty);

        Vendor exotic = new Vendor();
        exotic.setName("Exotic Fruits Company");
        vendorRepository.save(exotic);

        Vendor home = new Vendor();
        home.setName("Home fruits");
        vendorRepository.save(home);

        Vendor fun = new Vendor();
        fun.setName("Fun Fresh Fruits Ltd");
        vendorRepository.save(fun);

        Vendor nuts = new Vendor();
        nuts.setName("Nuts for Nuts Company");
        vendorRepository.save(nuts);

        System.out.println("Vendors loaded: " + vendorRepository.count());
    }
}
