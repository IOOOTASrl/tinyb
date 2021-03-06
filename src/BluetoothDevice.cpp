/*
 * Author: Petre Eftime <petre.p.eftime@intel.com>
 * Copyright (c) 2015 Intel Corporation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

#include "generated-code.h"
#include "tinyb_utils.hpp"
#include "BluetoothDevice.hpp"
#include "BluetoothGattService.hpp"
#include "BluetoothManager.hpp"

using namespace tinyb;

std::string BluetoothDevice::get_class_name() const
{
    return std::string("BluetoothDevice");
}

std::string BluetoothDevice::get_java_class() const
{
    return std::string(JAVA_PACKAGE "/BluetoothDevice");
}

std::string BluetoothDevice::get_object_path() const
{
    return std::string(g_dbus_proxy_get_object_path(G_DBUS_PROXY(object)));
}

BluetoothType BluetoothDevice::get_bluetooth_type() const
{
    return BluetoothType::DEVICE;
}

BluetoothDevice::BluetoothDevice(Device1 *object)
{
    this->object = object;
    g_object_ref(object);
}

BluetoothDevice::BluetoothDevice(const BluetoothDevice &object)
{
    BluetoothDevice(object.object);
}

BluetoothDevice::~BluetoothDevice()
{
    g_object_unref(object);
}

std::unique_ptr<BluetoothDevice> BluetoothDevice::make(Object *object,
    BluetoothType type, std::string *name, std::string *identifier,
    BluetoothObject *parent)
{
    Device1 *device;
    if((type == BluetoothType::NONE || type == BluetoothType::DEVICE) &&
        (device = object_get_device1(object)) != NULL) {

        std::unique_ptr<BluetoothDevice> p(new BluetoothDevice(device));

        if ((name == nullptr || *name == p->get_name()) &&
            (identifier == nullptr || *identifier == p->get_address()) &&
            (parent == nullptr || *parent == p->get_adapter()))
            return p;
    }

    return std::unique_ptr<BluetoothDevice>();
}

BluetoothDevice *BluetoothDevice::clone() const
{
    return new BluetoothDevice(object);
}

std::vector<std::unique_ptr<BluetoothGattService>> BluetoothDevice::get_services()
{
    std::vector<std::unique_ptr<BluetoothGattService>> vector;
    GList *l, *objects = g_dbus_object_manager_get_objects(gdbus_manager);

    for (l = objects; l != NULL; l = l->next) {
        Object *object = OBJECT(l->data);

        auto p = BluetoothGattService::make(object,
            BluetoothType::GATT_SERVICE, NULL, NULL, this);
        if (p != nullptr)
            vector.push_back(std::move(p));
    }

    return vector;
}

/* D-Bus method calls: */
bool BluetoothDevice::disconnect ()
{
    GError *error = NULL;
    bool result;
    result = device1_call_disconnect_sync(
        object,
        NULL,
        &error
    );
    if (error)
        g_printerr("Error: %s\n", error->message);
    return result;
}

bool BluetoothDevice::connect ()
{
    GError *error = NULL;
    bool result;
    result = device1_call_connect_sync(
        object,
        NULL,
        &error
    );
    if (error)
        g_printerr("Error: %s\n", error->message);
    return result;
}

bool BluetoothDevice::connect_profile (
    const std::string &arg_UUID)
{
    GError *error = NULL;
    bool result;
    result = device1_call_connect_profile_sync(
        object,
        arg_UUID.c_str(),
        NULL,
        &error
    );
    if (error)
        g_printerr("Error: %s\n", error->message);
    return result;
}

bool BluetoothDevice::disconnect_profile (
    const std::string &arg_UUID)
{
    GError *error = NULL;
    bool result;
    result = device1_call_disconnect_profile_sync(
        object,
        arg_UUID.c_str(),
        NULL,
        &error
    );
    if (error)
        g_printerr("Error: %s\n", error->message);
    return result;
}

bool BluetoothDevice::pair ()
{
    GError *error = NULL;
    bool result;
    result = device1_call_pair_sync(
        object,
        NULL,
        &error
    );
    if (error)
        g_printerr("Error: %s\n", error->message);
    return result;
}

bool BluetoothDevice::cancel_pairing ()
{
    GError *error = NULL;
    bool result;
    result = device1_call_cancel_pairing_sync(
        object,
        NULL,
        &error
    );
    if (error)
        g_printerr("Error: %s\n", error->message);
    return result;
}



/* D-Bus property accessors: */
std::string BluetoothDevice::get_address ()
{
    return std::string(device1_get_address (object));
}

std::string BluetoothDevice::get_name ()
{
    const gchar *name = device1_get_name(object);
    if (name == nullptr)
        return std::string(device1_get_alias(object));
    return std::string(name);
}

std::string BluetoothDevice::get_alias ()
{
    return device1_get_alias (object);
}

void BluetoothDevice::set_alias (const std::string &value)
{
    device1_set_alias (object, value.c_str());
}

unsigned int BluetoothDevice::get_class ()
{
    return device1_get_class (object);
}

uint16_t BluetoothDevice::get_appearance ()
{
    return device1_get_appearance (object);
}

std::string BluetoothDevice::get_icon ()
{
    return std::string(device1_get_icon (object));
}

bool BluetoothDevice::get_paired ()
{
    return device1_get_paired (object);
}

bool BluetoothDevice::get_trusted ()
{
    return device1_get_trusted (object);
}

void BluetoothDevice::set_trusted (bool  value)
{
    device1_set_trusted (object, value);
}

bool BluetoothDevice::get_blocked ()
{
    return device1_get_blocked (object);
}

void BluetoothDevice::set_blocked (bool  value)
{
    device1_set_blocked (object, value);
}

bool BluetoothDevice::get_legacy_pairing ()
{
    return device1_get_legacy_pairing (object);
}

int16_t BluetoothDevice::get_rssi ()
{
    return device1_get_rssi (object);
}

bool BluetoothDevice::get_connected ()
{
    return device1_get_connected (object);
}

std::vector<std::string> BluetoothDevice::get_uuids ()
{

    const char * const *uuids_c = device1_get_uuids (object);
    std::vector<std::string> uuids;
    for (int i = 0; uuids_c[i] != NULL ;i++)
        uuids.push_back(std::string(uuids_c[i]));
    return uuids;
}

std::string BluetoothDevice::get_modalias ()
{
    return std::string(device1_get_modalias (object));
}

BluetoothAdapter BluetoothDevice::get_adapter ()
{
    GError *error = NULL;

    Adapter1 *adapter = adapter1_proxy_new_for_bus_sync(
        G_BUS_TYPE_SYSTEM,
        G_DBUS_PROXY_FLAGS_NONE,
        "org.bluez",
        device1_get_adapter (object),
        NULL,
        &error);

   if (adapter == NULL) {
            g_printerr("Error instantiating adapter: %s",
                    error->message);
            g_error_free(error);
            throw std::exception();
   }

   return BluetoothAdapter(adapter);
}
