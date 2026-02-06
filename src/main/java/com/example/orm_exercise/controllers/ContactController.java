package com.example.orm_exercise.controllers;

import com.example.orm_exercise.models.Address;
import com.example.orm_exercise.models.Contact;
import com.example.orm_exercise.repositories.AddressRepository;
import com.example.orm_exercise.repositories.ContactRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;

    public ContactController(ContactRepository contactRepository,  AddressRepository addressRepository1)

    {
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository1;
    }

    @GetMapping
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable int id) {
        return contactRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Contact createContact(@RequestBody Contact contact) {

        if (contact.getAddresses() != null) {
            for (Address address : contact.getAddresses()) {
                address.setContact(contact);
            }
        }

        return contactRepository.save(contact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable int id, @RequestBody Contact updatedContact) {
        return contactRepository.findById(id).map(contact -> {
            contact.setName(updatedContact.getName());
            contact.setEmail(updatedContact.getEmail());
            contact.setPhoneNumber(updatedContact.getPhoneNumber());
            return contactRepository.save(contact);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable int id) {
        contactRepository.deleteById(id);
    }



    // 2️⃣ GET all addresses for a contact
    @GetMapping("/{contactId}/addresses")
    public List<Address> getAddressesForContact(@PathVariable int contactId) {

        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        return contact.getAddresses();
    }
}
