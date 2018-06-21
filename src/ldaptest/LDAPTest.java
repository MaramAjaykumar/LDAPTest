/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ldaptest;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

/**
 *
 * @author Administrator
 */
public class LDAPTest {

    static void printAttrs(Attributes attrs) {
        if (attrs == null) {
            System.out.println("No attributes");
        } else {
            /* Print each attribute */
            try {
                for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
                    Attribute attr = (Attribute) ae.next();
                    System.out.println("attribute: " + attr.getID());
                    DirContext attributeDefinition = attr.getAttributeDefinition();
                    Attributes attributes = attributeDefinition.getAttributes("");
                    NamingEnumeration<? extends Attribute> all = attributes.getAll();
                    while(all.hasMore())
                        System.out.println(all.next());
                    System.out.println("==============================");
                    /* print each value */
                    //                    for (NamingEnumeration e = attr.getAll(); e.hasMore(); System.out.println("value: " + e.next()));
                }
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Hashtable<String, Object> env = new Hashtable<String, Object>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=root");
        env.put(Context.SECURITY_CREDENTIALS, "Password1");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://192.168.3.126:389");
        try {
            // Create the initial context
            DirContext ctx = new InitialDirContext(env);

            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "(&(objectClass=*))";
            NamingEnumeration answer = ctx.search("cn=test_usr_realm,dc=com", searchFilter, searchCtls);
             
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                printAttrs(attrs);
//                if (attrs != null) {
//                    NamingEnumeration ae = attrs.getAll();
//                    while (ae.hasMore()) {
//                        Attribute attr = (Attribute) ae.next();
//                        System.out.println("Attribute  " + attr.getID());
//                    }
//                }
            }
            // Close the context when we're done
            ctx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
