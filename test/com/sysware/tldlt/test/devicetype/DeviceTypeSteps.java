package com.sysware.tldlt.test.devicetype;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.sysware.tldlt.app.web.devicetype.DeviceTypeAction;

/**
 * Type：DeviceTypeSteps
 * Descript：设备类型Steps.
 * Create：SW-ITS-HHE Create
 * Time：2015年7月10日 上午10:54:32
 * Version：@version
 */
public class DeviceTypeSteps {
    private DeviceTypeAction deviceTypeAction;

    @AfterScenario
    public void tearDown() {
        deviceTypeAction = null;
    }

    @Given("show main page")
    public void givenShowMainPage() {

    }

    @Then("show device type infos page and show all device type info ")
    public void thenShowDeviceTypeInfosPageAndShowAllDeviceTypeInfo() {

    }

    @When("click device type menu")
    public void whenClickDeviceTypeMenu() {
        deviceTypeAction = new DeviceTypeAction();
    }
}