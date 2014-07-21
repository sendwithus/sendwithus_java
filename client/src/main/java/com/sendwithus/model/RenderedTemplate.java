package com.sendwithus.model;


/**
 * This class was originally authored using public properties (sans getters/setters).
 * 
 * Deprecating direct use of these properties to discourage ongoing use.
 */
public class RenderedTemplate extends APIReceipt {

    /**
     * @deprecated Don't use properties directly; Use getter methods instead.
     */
    @Deprecated
    public String subject; // must match JSON response
    
    /**
     * @deprecated Don't use properties directly; Use getter methods instead.
     */
    @Deprecated
    public String html; // must match JSON response
    
    /**
     * @deprecated Don't use properties directly; Use getter methods instead.
     */
    @Deprecated
    public String text; // must match JSON response
    
    /**
     * @deprecated Don't use properties directly; Use getter methods instead.
     */
    @Deprecated
    public Template template; // must match JSON response

    public class Template {
        
        /**
         * @deprecated Don't use properties directly; Use getter methods instead.
         */
        @Deprecated
        public String name; // must match JSON response
        
        /**
         * @deprecated Don't use properties directly; Use getter methods instead.
         */
        @Deprecated
        public String version_name; // must match JSON response
        
        public String getName() {
            return name;
        }
        
        public String getVersion_name() {
            return version_name;
        }
    }
    
    public String getSubject() {
        return subject;
    }

    public String getHtml() {
        return html;
    }

    public String getText() {
        return text;
    }

    public Template getTemplate() {
        return template;
    }

    public String toString() {
        return String.format("RenderedTemplate[%s]", getTemplate().getName());
    }
    
}
