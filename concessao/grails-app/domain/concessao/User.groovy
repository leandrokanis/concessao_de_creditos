package concessao

class User {

	String name
	String registration
	String email
	Invoice invoice

    static constraints = {
    }

    String toString() {
    	return name
    }
}
