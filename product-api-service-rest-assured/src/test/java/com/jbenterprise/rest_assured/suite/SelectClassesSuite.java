package com.jbenterprise.rest_assured.suite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import com.jbenterprise.rest_assured.tests.*;

@Suite
@SelectClasses( {BasicTest.class} )
public class SelectClassesSuite {

}
