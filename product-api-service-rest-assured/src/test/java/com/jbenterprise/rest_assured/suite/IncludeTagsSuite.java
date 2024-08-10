package com.jbenterprise.rest_assured.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.jbenterprise.rest_assured.tests")
@IncludeTags("Basic")
public class IncludeTagsSuite {

}
