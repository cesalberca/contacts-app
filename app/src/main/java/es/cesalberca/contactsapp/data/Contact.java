package es.cesalberca.contactsapp.data;

import java.util.UUID;

/**
 * Inmutable model class for Contact
 */
public final class Contact {
    private String id;
    private String name;
    private String lastname;
    private String phone;
    private String address;
    private String avatar;
    private String postalCode;
    private String email;

    /**
     * Constructor used to create new Contacts
     * @param name Name of the contact
     * @param lastname Lastname of the contact
     * @param phone Phone number of the contact
     * @param address Address of the contact
     * @param avatar Path to the avatar photo of that contact
     */
    public Contact(String name, String lastname, String phone, String address, String avatar, String postalCode, String email) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.postalCode = postalCode;
        this.email = email;
    }

    /**
     * Constructor used for retrieving contacts from the database
     * @param id Id of the contact
     * @param name Name of the contact
     * @param lastname Lastname of the contact
     * @param phone Phone number of the contact
     * @param address Address of the contact
     * @param avatar Path to the avatar photo of that contact
     */
    public Contact(String id, String name, String lastname, String phone, String address, String avatar, String postalCode, String email) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.address = address;
        this.avatar = avatar;
        this.postalCode = postalCode;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (id != null ? !id.equals(contact.id) : contact.id != null) return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (lastname != null ? !lastname.equals(contact.lastname) : contact.lastname != null)
            return false;
        if (phone != null ? !phone.equals(contact.phone) : contact.phone != null) return false;
        if (address != null ? !address.equals(contact.address) : contact.address != null)
            return false;
        if (avatar != null ? !avatar.equals(contact.avatar) : contact.avatar != null) return false;
        if (postalCode != null ? !postalCode.equals(contact.postalCode) : contact.postalCode != null)
            return false;
        return email != null ? email.equals(contact.email) : contact.email == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + "\t" +
               lastname + "\t" +
               phone + "\t" +
               address + "\t" +
               postalCode + "\t" +
               email + "\t";
    }
}
