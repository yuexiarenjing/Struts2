/*
 * Copyright 2002-2006,2009 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opensymphony.xwork2.interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.SimpleFooAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.mock.MockActionInvocation;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.io.Serializable;

/**
 * Unit test for I18nInterceptor.
 *
 * @author Claus Ibsen
 */
public class I18nInterceptorTest extends TestCase {

    private I18nInterceptor interceptor;
    private ActionContext ac;
    private Map<String, Serializable> params;
    private Map session;
    private ActionInvocation mai;

    public void testEmptyParamAndSession() throws Exception {
        interceptor.intercept(mai);
    }

    public void testNoSession() throws Exception {
        ac.setSession(null);
        interceptor.intercept(mai);
    }

    public void testDefaultLocale() throws Exception {
        params.put(I18nInterceptor.DEFAULT_PARAMETER, "_"); // bad locale that would get us default locale instead
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        assertNotNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should be stored here
        assertEquals(Locale.getDefault(), session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should create a locale object
    }

    public void testDenmarkLocale() throws Exception {
        params.put(I18nInterceptor.DEFAULT_PARAMETER, "da_DK");
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        Locale denmark = new Locale("da", "DK");
        assertNotNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should be stored here
        assertEquals(denmark, session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should create a locale object
    }

    public void testDenmarkLocaleRequestOnly() throws Exception {
        params.put(I18nInterceptor.DEFAULT_REQUESTONLY_PARAMETER, "da_DK");
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        Locale denmark = new Locale("da", "DK");
        assertNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should be stored here
        assertEquals(denmark, mai.getInvocationContext().getLocale()); // should create a locale object
    }

    public void testCountryOnlyLocale() throws Exception {
        params.put(I18nInterceptor.DEFAULT_PARAMETER, "DK");
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        Locale denmark = new Locale("DK");
        assertNotNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should be stored here
        assertEquals(denmark, session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should create a locale object
    }

    public void testLanguageOnlyLocale() throws Exception {
        params.put(I18nInterceptor.DEFAULT_PARAMETER, "da_");
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        Locale denmark = new Locale("da");
        assertNotNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should be stored here
        assertEquals(denmark, session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should create a locale object
    }

    public void testWithVariant() throws Exception {
        params.put(I18nInterceptor.DEFAULT_PARAMETER, "fr_CA_xx");
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        Locale variant = new Locale("fr", "CA", "xx");
        Locale locale = (Locale) session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
        assertNotNull(locale); // should be stored here
        assertEquals(variant, locale);
        assertEquals("xx", locale.getVariant());
    }

    public void testWithVariantRequestOnly() throws Exception {
        params.put(I18nInterceptor.DEFAULT_REQUESTONLY_PARAMETER, "fr_CA_xx");
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed
        assertNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE));

        Locale variant = new Locale("fr", "CA", "xx");
        Locale locale = mai.getInvocationContext().getLocale();
        assertNotNull(locale); // should be stored here
        assertEquals(variant, locale);
        assertEquals("xx", locale.getVariant());
    }

    public void testRealLocaleObjectInParams() throws Exception {
        params.put(I18nInterceptor.DEFAULT_PARAMETER, Locale.CANADA_FRENCH);
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        assertNotNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should be stored here
        assertEquals(Locale.CANADA_FRENCH, session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should create a locale object
    }

    public void testRealLocalesInParams() throws Exception {
        Locale[] locales = new Locale[] { Locale.CANADA_FRENCH };
        assertTrue(locales.getClass().isArray());
        params.put(I18nInterceptor.DEFAULT_PARAMETER, locales);
        interceptor.intercept(mai);

        assertNull(params.get(I18nInterceptor.DEFAULT_PARAMETER)); // should have been removed

        assertNotNull(session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE)); // should be stored here
        assertEquals(Locale.CANADA_FRENCH, session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE));
    }

    public void testSetParameterAndAttributeNames() throws Exception {
        interceptor.setAttributeName("hello");
        interceptor.setParameterName("world");

        params.put("world", Locale.CHINA);
        interceptor.intercept(mai);

        assertNull(params.get("world")); // should have been removed

        assertNotNull(session.get("hello")); // should be stored here
        assertEquals(Locale.CHINA, session.get("hello"));
    }

    public void testActionContextLocaleIsPreservedWhenNotOverridden() throws Exception {
        final Locale locale1 = Locale.TRADITIONAL_CHINESE;
        mai.getInvocationContext().setLocale(locale1);
        interceptor.intercept(mai);

        Locale locale = (Locale) session.get(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
        assertNull(locale); // should not be stored here
        locale = mai.getInvocationContext().getLocale();
        assertEquals(locale1, locale);
    }

    @Override
    protected void setUp() throws Exception {
        interceptor = new I18nInterceptor();
        interceptor.init();
        params = new HashMap<String, Serializable>();
        session = new HashMap();

        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put(ActionContext.PARAMETERS, params);
        ctx.put(ActionContext.SESSION, session);
        ac = new ActionContext(ctx);

        Action action = new SimpleFooAction();
        mai = new MockActionInvocation();
        ((MockActionInvocation) mai).setAction(action);
        ((MockActionInvocation) mai).setInvocationContext(ac);
    }

    @Override
    protected void tearDown() throws Exception {
        interceptor.destroy();
        interceptor = null;
        ac = null;
        params = null;
        session = null;
        mai = null;
    }

}
