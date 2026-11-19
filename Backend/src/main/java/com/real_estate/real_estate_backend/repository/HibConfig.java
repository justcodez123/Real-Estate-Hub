package com.real_estate.real_estate_backend.repository;

import com.real_estate.real_estate_backend.controller.enums.UserRole;
import com.real_estate.real_estate_backend.models.Users;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibConfig {
    /*
    *   @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String phone;

    //For the Subscription Logic later
    private String subscriptionType = "BASIC";
    * */
    public static void main(String[] args) {
        Users u1 = new Users();
        //u1.setId(5L);
        u1.setFirstName("Aashish");
        u1.setLastName("Kaaley");
        u1.setEmail("aashishkaaley@gmail.com");
        u1.setPassword("ABC555");
        u1.setPhone("1234567891");

        Configuration cfg = new Configuration()
                .configure().
                addAnnotatedClass(com.real_estate.real_estate_backend.models.Users.class);

        try(SessionFactory sf = cfg.buildSessionFactory()){
            Session session = sf.openSession();
            Transaction tr = session.beginTransaction();
            /*To save the data in the database*/
            //session.persist(u1);
            System.out.println("------------------------");
            /*To update and insert(if not found) the data*/
            session.merge(u1);
            System.out.println("-------------------------");
            tr.commit();

            /*To fetch */
            session.get("users", u1);
            System.out.println(u1);
        }catch (HibernateException e){
            e.getCause();
        }
    }

}
