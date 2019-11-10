package com.grafsemmel.valium

import org.powermock.api.mockito.PowerMockito

object TestUtils {
    fun mockField(fieldName: String, clazz: Class<*>, obj: Any) = PowerMockito.field(clazz, fieldName).set(clazz, obj)
}