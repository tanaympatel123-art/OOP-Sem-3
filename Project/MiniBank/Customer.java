class Customer implements Cloneable {
    private static long customerCounter = 101;

    private final String customerId;
    private final String name;
    private final String email;
    private final String mobile;
    private final Address address;

    Customer(String name, String email, String mobile, Address address) {
        this.customerId = "CUST" + customerCounter++;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    String getCustomerId() {
        return customerId;
    }

    String getName() {
        return name;
    }

    String getEmail() {
        return email;
    }

    String getMobile() {
        return mobile;
    }

    Address getAddress() {
        return address;
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
    }

    static class Address {
        private final String line;
        private final String city;
        private final String pincode;

        Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        String getLine() {
            return line;
        }

        String getCity() {
            return city;
        }

        String getPincode() {
            return pincode;
        }
    }
}
