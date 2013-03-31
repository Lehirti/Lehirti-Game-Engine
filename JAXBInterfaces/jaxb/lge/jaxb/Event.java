//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.31 at 10:47:12 PM CEST 
//


package lge.jaxb;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="todo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extensions" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="extension" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="event" use="required" type="{}event_reference" />
 *                           &lt;attribute name="key" type="{}key_type" />
 *                           &lt;attribute name="text" use="required" type="{}image_or_text_reference" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="images" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="bg" type="{}image_or_text_reference" minOccurs="0"/>
 *                   &lt;element name="fg" type="{}image_or_text_reference" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="clearBackground" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *                 &lt;attribute name="clearForeground" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="texts">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="text" type="{}image_or_text_reference" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="options">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="option" maxOccurs="12">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="key" type="{}key_type" />
 *                           &lt;attribute name="text" use="required" type="{}image_or_text_reference" />
 *                           &lt;attribute name="event" use="required" type="{}event_reference" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "todo",
    "extensions",
    "images",
    "texts",
    "options"
})
@XmlRootElement(name = "event")
public class Event {

    protected String description;
    protected String todo;
    protected Event.Extensions extensions;
    protected Event.Images images;
    @XmlElement(required = true)
    protected Event.Texts texts;
    @XmlElement(required = true)
    protected Event.Options options;
    @XmlAttribute(name = "version")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger version;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the todo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTodo() {
        return todo;
    }

    /**
     * Sets the value of the todo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTodo(String value) {
        this.todo = value;
    }

    /**
     * Gets the value of the extensions property.
     * 
     * @return
     *     possible object is
     *     {@link Event.Extensions }
     *     
     */
    public Event.Extensions getExtensions() {
        return extensions;
    }

    /**
     * Sets the value of the extensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.Extensions }
     *     
     */
    public void setExtensions(Event.Extensions value) {
        this.extensions = value;
    }

    /**
     * Gets the value of the images property.
     * 
     * @return
     *     possible object is
     *     {@link Event.Images }
     *     
     */
    public Event.Images getImages() {
        return images;
    }

    /**
     * Sets the value of the images property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.Images }
     *     
     */
    public void setImages(Event.Images value) {
        this.images = value;
    }

    /**
     * Gets the value of the texts property.
     * 
     * @return
     *     possible object is
     *     {@link Event.Texts }
     *     
     */
    public Event.Texts getTexts() {
        return texts;
    }

    /**
     * Sets the value of the texts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.Texts }
     *     
     */
    public void setTexts(Event.Texts value) {
        this.texts = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link Event.Options }
     *     
     */
    public Event.Options getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link Event.Options }
     *     
     */
    public void setOptions(Event.Options value) {
        this.options = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVersion(BigInteger value) {
        this.version = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="extension" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="event" use="required" type="{}event_reference" />
     *                 &lt;attribute name="key" type="{}key_type" />
     *                 &lt;attribute name="text" use="required" type="{}image_or_text_reference" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "extension"
    })
    public static class Extensions {

        @XmlElement(required = true)
        protected List<Event.Extensions.Extension> extension;

        /**
         * Gets the value of the extension property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the extension property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExtension().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Event.Extensions.Extension }
         * 
         * 
         */
        public List<Event.Extensions.Extension> getExtension() {
            if (extension == null) {
                extension = new ArrayList<Event.Extensions.Extension>();
            }
            return this.extension;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="event" use="required" type="{}event_reference" />
         *       &lt;attribute name="key" type="{}key_type" />
         *       &lt;attribute name="text" use="required" type="{}image_or_text_reference" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Extension {

            @XmlAttribute(name = "event", required = true)
            protected String event;
            @XmlAttribute(name = "key")
            protected KeyType key;
            @XmlAttribute(name = "text", required = true)
            protected String text;

            /**
             * Gets the value of the event property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getEvent() {
                return event;
            }

            /**
             * Sets the value of the event property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setEvent(String value) {
                this.event = value;
            }

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link KeyType }
             *     
             */
            public KeyType getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link KeyType }
             *     
             */
            public void setKey(KeyType value) {
                this.key = value;
            }

            /**
             * Gets the value of the text property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getText() {
                return text;
            }

            /**
             * Sets the value of the text property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setText(String value) {
                this.text = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="bg" type="{}image_or_text_reference" minOccurs="0"/>
     *         &lt;element name="fg" type="{}image_or_text_reference" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="clearBackground" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *       &lt;attribute name="clearForeground" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bg",
        "fg"
    })
    public static class Images {

        protected String bg;
        protected List<String> fg;
        @XmlAttribute(name = "clearBackground")
        protected Boolean clearBackground;
        @XmlAttribute(name = "clearForeground")
        protected Boolean clearForeground;

        /**
         * Gets the value of the bg property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBg() {
            return bg;
        }

        /**
         * Sets the value of the bg property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBg(String value) {
            this.bg = value;
        }

        /**
         * Gets the value of the fg property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the fg property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFg().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getFg() {
            if (fg == null) {
                fg = new ArrayList<String>();
            }
            return this.fg;
        }

        /**
         * Gets the value of the clearBackground property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isClearBackground() {
            if (clearBackground == null) {
                return false;
            } else {
                return clearBackground;
            }
        }

        /**
         * Sets the value of the clearBackground property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setClearBackground(Boolean value) {
            this.clearBackground = value;
        }

        /**
         * Gets the value of the clearForeground property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isClearForeground() {
            if (clearForeground == null) {
                return true;
            } else {
                return clearForeground;
            }
        }

        /**
         * Sets the value of the clearForeground property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setClearForeground(Boolean value) {
            this.clearForeground = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="option" maxOccurs="12">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="key" type="{}key_type" />
     *                 &lt;attribute name="text" use="required" type="{}image_or_text_reference" />
     *                 &lt;attribute name="event" use="required" type="{}event_reference" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "option"
    })
    public static class Options {

        @XmlElement(required = true)
        protected List<Event.Options.Option> option;

        /**
         * Gets the value of the option property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the option property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getOption().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Event.Options.Option }
         * 
         * 
         */
        public List<Event.Options.Option> getOption() {
            if (option == null) {
                option = new ArrayList<Event.Options.Option>();
            }
            return this.option;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="key" type="{}key_type" />
         *       &lt;attribute name="text" use="required" type="{}image_or_text_reference" />
         *       &lt;attribute name="event" use="required" type="{}event_reference" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Option {

            @XmlAttribute(name = "key")
            protected KeyType key;
            @XmlAttribute(name = "text", required = true)
            protected String text;
            @XmlAttribute(name = "event", required = true)
            protected String event;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link KeyType }
             *     
             */
            public KeyType getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link KeyType }
             *     
             */
            public void setKey(KeyType value) {
                this.key = value;
            }

            /**
             * Gets the value of the text property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getText() {
                return text;
            }

            /**
             * Sets the value of the text property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setText(String value) {
                this.text = value;
            }

            /**
             * Gets the value of the event property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getEvent() {
                return event;
            }

            /**
             * Sets the value of the event property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setEvent(String value) {
                this.event = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="text" type="{}image_or_text_reference" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "text"
    })
    public static class Texts {

        @XmlElement(required = true)
        protected List<String> text;

        /**
         * Gets the value of the text property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the text property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getText().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getText() {
            if (text == null) {
                text = new ArrayList<String>();
            }
            return this.text;
        }

    }

}
