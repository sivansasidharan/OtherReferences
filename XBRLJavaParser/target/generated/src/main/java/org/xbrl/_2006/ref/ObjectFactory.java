//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.19 at 02:44:26 PM IST 
//


package org.xbrl._2006.ref;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.xbrl._2006.ref package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Publisher_QNAME = new QName("http://www.xbrl.org/2006/ref", "Publisher");
    private final static QName _Paragraph_QNAME = new QName("http://www.xbrl.org/2006/ref", "Paragraph");
    private final static QName _Clause_QNAME = new QName("http://www.xbrl.org/2006/ref", "Clause");
    private final static QName _URIDate_QNAME = new QName("http://www.xbrl.org/2006/ref", "URIDate");
    private final static QName _IssueDate_QNAME = new QName("http://www.xbrl.org/2006/ref", "IssueDate");
    private final static QName _Chapter_QNAME = new QName("http://www.xbrl.org/2006/ref", "Chapter");
    private final static QName _Appendix_QNAME = new QName("http://www.xbrl.org/2006/ref", "Appendix");
    private final static QName _Footnote_QNAME = new QName("http://www.xbrl.org/2006/ref", "Footnote");
    private final static QName _Subsection_QNAME = new QName("http://www.xbrl.org/2006/ref", "Subsection");
    private final static QName _Note_QNAME = new QName("http://www.xbrl.org/2006/ref", "Note");
    private final static QName _Example_QNAME = new QName("http://www.xbrl.org/2006/ref", "Example");
    private final static QName _URI_QNAME = new QName("http://www.xbrl.org/2006/ref", "URI");
    private final static QName _Section_QNAME = new QName("http://www.xbrl.org/2006/ref", "Section");
    private final static QName _Number_QNAME = new QName("http://www.xbrl.org/2006/ref", "Number");
    private final static QName _Subclause_QNAME = new QName("http://www.xbrl.org/2006/ref", "Subclause");
    private final static QName _Article_QNAME = new QName("http://www.xbrl.org/2006/ref", "Article");
    private final static QName _Exhibit_QNAME = new QName("http://www.xbrl.org/2006/ref", "Exhibit");
    private final static QName _Page_QNAME = new QName("http://www.xbrl.org/2006/ref", "Page");
    private final static QName _Name_QNAME = new QName("http://www.xbrl.org/2006/ref", "Name");
    private final static QName _Subparagraph_QNAME = new QName("http://www.xbrl.org/2006/ref", "Subparagraph");
    private final static QName _Sentence_QNAME = new QName("http://www.xbrl.org/2006/ref", "Sentence");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.xbrl._2006.ref
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Publisher", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createPublisher(String value) {
        return new JAXBElement<String>(_Publisher_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Paragraph", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createParagraph(String value) {
        return new JAXBElement<String>(_Paragraph_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Clause", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createClause(String value) {
        return new JAXBElement<String>(_Clause_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "URIDate", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createURIDate(String value) {
        return new JAXBElement<String>(_URIDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "IssueDate", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createIssueDate(String value) {
        return new JAXBElement<String>(_IssueDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Chapter", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createChapter(String value) {
        return new JAXBElement<String>(_Chapter_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Appendix", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createAppendix(String value) {
        return new JAXBElement<String>(_Appendix_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Footnote", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createFootnote(String value) {
        return new JAXBElement<String>(_Footnote_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Subsection", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createSubsection(String value) {
        return new JAXBElement<String>(_Subsection_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Note", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createNote(String value) {
        return new JAXBElement<String>(_Note_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Example", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createExample(String value) {
        return new JAXBElement<String>(_Example_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "URI", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createURI(String value) {
        return new JAXBElement<String>(_URI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Section", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createSection(String value) {
        return new JAXBElement<String>(_Section_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Number", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createNumber(String value) {
        return new JAXBElement<String>(_Number_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Subclause", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createSubclause(String value) {
        return new JAXBElement<String>(_Subclause_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Article", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createArticle(String value) {
        return new JAXBElement<String>(_Article_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Exhibit", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createExhibit(String value) {
        return new JAXBElement<String>(_Exhibit_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Page", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createPage(String value) {
        return new JAXBElement<String>(_Page_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Name", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Subparagraph", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createSubparagraph(String value) {
        return new JAXBElement<String>(_Subparagraph_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xbrl.org/2006/ref", name = "Sentence", substitutionHeadNamespace = "http://www.xbrl.org/2003/linkbase", substitutionHeadName = "part")
    public JAXBElement<String> createSentence(String value) {
        return new JAXBElement<String>(_Sentence_QNAME, String.class, null, value);
    }

}