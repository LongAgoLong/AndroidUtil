package com.leo.system.util

import java.lang.reflect.*
import java.sql.SQLException
import java.util.*
import java.util.regex.Pattern

/**
 * Created by LEO
 * On 2019/6/16
 * Description:反射工具类
 */
object ReflectHelper {

    /**
     * Pattern for detecting CGLIB-renamed methods.
     *
     * @see .isCglibRenamedMethod
     */
    private val CGLIB_RENAMED_METHOD_PATTERN = Pattern.compile("CGLIB\\$(.+)\\$\\d+")


    /**
     * Pre-built FieldFilter that matches all non-static, non-final fields.
     */
    var COPYABLE_FIELDS: FieldFilter = object : FieldFilter {

        override fun matches(field: Field): Boolean {
            return !(Modifier.isStatic(field.modifiers) || Modifier.isFinal(field.modifiers))
        }
    }


    /**
     * Pre-built MethodFilter that matches all non-bridge methods.
     */
    var NON_BRIDGED_METHODS: MethodFilter = object : MethodFilter {

        override fun matches(method: Method): Boolean {
            return !method.isBridge
        }
    }


    /**
     * Pre-built MethodFilter that matches all non-bridge methods
     * which are not declared on `java.lang.Object`.
     */
    var USER_DECLARED_METHODS: MethodFilter = object : MethodFilter {

        override fun matches(method: Method): Boolean {
            return !method.isBridge && method.declaringClass != Any::class.java
        }
    }

    /**
     * Attempt to find a [field][Field] on the supplied [Class] with the
     * supplied `name` and/or [type][Class]. Searches all superclasses
     * up to [Object].
     *
     * @param clazz the class to introspect
     * @param name  the name of the field (may be `null` if type is specified)
     * @param type  the type of the field (may be `null` if name is specified)
     * @return the corresponding Field object, or `null` if not found
     */
    @JvmOverloads
    fun findField(clazz: Class<*>, name: String?, type: Class<*>? = null): Field? {
        //Assert.notNull(clazz, "Class must not be null");
        //Assert.isTrue(name != null || type != null, "Either name or type of the field must be specified");
        var searchType: Class<*>? = clazz
        while (Any::class.java != searchType && searchType != null) {
            val fields = searchType.declaredFields
            for (field in fields) {
                if ((name == null || name == field.name) && (type == null || type == field.type)) {
                    return field
                }
            }
            searchType = searchType.superclass
        }
        return null
    }

    /**
     * Set the field represented by the supplied [field object][Field] on the
     * specified [target object][Object] to the specified `value`.
     * In accordance with [Field.set] semantics, the new value
     * is automatically unwrapped if the underlying field has a primitive type.
     *
     * Thrown exceptions are handled via a call to [.handleReflectionException].
     *
     * @param field  the field to set
     * @param target the target object on which to set the field
     * @param value  the value to set; may be `null`
     */
    fun setField(field: Field, target: Any, value: Any) {
        try {
            field.set(target, value)
        } catch (ex: IllegalAccessException) {
            handleReflectionException(ex)
            throw IllegalStateException(
                "Unexpected reflection exception - " + ex.javaClass.name + ": " + ex.message
            )
        }

    }

    /**
     * Get the field represented by the supplied [field object][Field] on the
     * specified [target object][Object]. In accordance with [Field.get]
     * semantics, the returned value is automatically wrapped if the underlying field
     * has a primitive type.
     *
     * Thrown exceptions are handled via a call to [.handleReflectionException].
     *
     * @param field  the field to get
     * @param target the target object from which to get the field
     * @return the field's current value
     */
    fun getField(field: Field, target: Any): Any {
        try {
            return field.get(target)
        } catch (ex: IllegalAccessException) {
            handleReflectionException(ex)
            throw IllegalStateException(
                "Unexpected reflection exception - " + ex.javaClass.name + ": " + ex.message
            )
        }

    }

    /**
     * Attempt to find a [Method] on the supplied class with the supplied name
     * and parameter types. Searches all superclasses up to `Object`.
     *
     * Returns `null` if no [Method] can be found.
     *
     * @param clazz      the class to introspect
     * @param name       the name of the method
     * @param paramTypes the parameter types of the method
     * (may be `null` to indicate any signature)
     * @return the Method object, or `null` if none found
     */
    fun findMethod(clazz: Class<*>, name: String): Method? {
        //Assert.notNull(clazz, "Class must not be null");
        //Assert.notNull(name, "Method name must not be null");
        var searchType: Class<*>? = clazz
        while (searchType != null) {
            val methods =
                if (searchType.isInterface) searchType.methods else searchType.declaredMethods
            for (method in methods) {
                if (name == method.name) {
                    return method
                }
            }
            searchType = searchType.superclass
        }
        return null
    }

    fun findMethod(clazz: Class<*>, name: String, vararg paramTypes: Class<*>): Method? {
        //Assert.notNull(clazz, "Class must not be null");
        //Assert.notNull(name, "Method name must not be null");
        var searchType: Class<*>? = clazz
        while (searchType != null) {
            val methods =
                if (searchType.isInterface) searchType.methods else searchType.declaredMethods
            for (method in methods) {
                if (name == method.name && Arrays.equals(paramTypes, method.parameterTypes)) {
                    return method
                }
            }
            searchType = searchType.superclass
        }
        return null
    }

    /**
     * Invoke the specified [Method] against the supplied target object with the
     * supplied arguments. The target object can be `null` when invoking a
     * static [Method].
     *
     * Thrown exceptions are handled via a call to [.handleReflectionException].
     *
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @param args   the invocation arguments (may be `null`)
     * @return the invocation result, if any
     */
    fun invokeMethod(method: Method, target: Any, vararg args: Any): Any {
        try {
            return method.invoke(target, *args)
        } catch (ex: Exception) {
            handleReflectionException(ex)
        }

        throw IllegalStateException("Should never get here")
    }

    /**
     * Invoke the specified JDBC API [Method] against the supplied target
     * object with the supplied arguments.
     *
     * @param method the method to invoke
     * @param target the target object to invoke the method on
     * @param args   the invocation arguments (may be `null`)
     * @return the invocation result, if any
     * @throws SQLException the JDBC API SQLException to rethrow (if any)
     * @see .invokeMethod
     */
    @Throws(SQLException::class)
    fun invokeJdbcMethod(method: Method, target: Objects, vararg args: Any): Any {
        try {
            return method.invoke(target, *args)
        } catch (ex: IllegalAccessException) {
            handleReflectionException(ex)
        } catch (ex: InvocationTargetException) {
            if (ex.targetException is SQLException) {
                throw ex.targetException as SQLException
            }
            handleInvocationTargetException(ex)
        }

        throw IllegalStateException("Should never get here")
    }

    /**
     * Handle the given reflection exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     *
     * Throws the underlying RuntimeException or Error in case of an
     * InvocationTargetException with such a root cause. Throws an
     * IllegalStateException with an appropriate message else.
     *
     * @param ex the reflection exception to handle
     */
    fun handleReflectionException(ex: Exception) {
        check(ex !is NoSuchMethodException) { "Method not found: " + ex.message }
        check(ex !is IllegalAccessException) { "Could not access method: " + ex.message }
        if (ex is InvocationTargetException) {
            handleInvocationTargetException(ex)
        }
        if (ex is RuntimeException) {
            throw ex
        }
        throw UndeclaredThrowableException(ex)
    }

    /**
     * Handle the given invocation target exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     *
     * Throws the underlying RuntimeException or Error in case of such a root
     * cause. Throws an IllegalStateException else.
     *
     * @param ex the invocation target exception to handle
     */
    fun handleInvocationTargetException(ex: InvocationTargetException) {
        rethrowRuntimeException(ex.targetException)
    }

    /**
     * Rethrow the given [exception][Throwable], which is presumably the
     * *target exception* of an [InvocationTargetException]. Should
     * only be called if no checked exception is expected to be thrown by the
     * target method.
     *
     * Rethrows the underlying exception cast to an [RuntimeException] or
     * [Error] if appropriate; otherwise, throws an
     * [IllegalStateException].
     *
     * @param ex the exception to rethrow
     * @throws RuntimeException the rethrown exception
     */
    fun rethrowRuntimeException(ex: Throwable) {
        if (ex is RuntimeException) {
            throw ex
        }
        if (ex is Error) {
            throw ex
        }
        throw UndeclaredThrowableException(ex)
    }

    /**
     * Rethrow the given [exception][Throwable], which is presumably the
     * *target exception* of an [InvocationTargetException]. Should
     * only be called if no checked exception is expected to be thrown by the
     * target method.
     *
     * Rethrows the underlying exception cast to an [Exception] or
     * [Error] if appropriate; otherwise, throws an
     * [IllegalStateException].
     *
     * @param ex the exception to rethrow
     * @throws Exception the rethrown exception (in case of a checked exception)
     */
    @Throws(Exception::class)
    fun rethrowException(ex: Throwable) {
        if (ex is Exception) {
            throw ex
        }
        if (ex is Error) {
            throw ex
        }
        throw UndeclaredThrowableException(ex)
    }

    /**
     * Determine whether the given method explicitly declares the given
     * exception or one of its superclasses, which means that an exception of
     * that type can be propagated as-is within a reflective invocation.
     *
     * @param method        the declaring method
     * @param exceptionType the exception to throw
     * @return `true` if the exception can be thrown as-is;
     * `false` if it needs to be wrapped
     */
    fun declaresException(method: Method, exceptionType: Class<*>): Boolean {
        //Assert.notNull(method, "Method must not be null");
        val declaredExceptions = method.exceptionTypes
        for (declaredException in declaredExceptions) {
            if (declaredException.isAssignableFrom(exceptionType)) {
                return true
            }
        }
        return false
    }

    /**
     * Determine whether the given field is a "public static final" constant.
     *
     * @param field the field to check
     */
    fun isPublicStaticFinal(field: Field): Boolean {
        val modifiers = field.modifiers
        return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(
            modifiers
        )
    }

    /**
     * Determine whether the given method is an "equals" method.
     *
     * @see Object.equals
     */
    fun isEqualsMethod(method: Method?): Boolean {
        if (method == null || method.name != "equals") {
            return false
        }
        val paramTypes = method.parameterTypes
        return paramTypes.size == 1 && paramTypes[0] == Any::class.java
    }

    /**
     * Determine whether the given method is a "hashCode" method.
     *
     * @see Object.hashCode
     */
    fun isHashCodeMethod(method: Method?): Boolean {
        return method != null && method.name == "hashCode" && method.parameterTypes.size == 0
    }

    /**
     * Determine whether the given method is a "toString" method.
     *
     * @see Object.toString
     */
    fun isToStringMethod(method: Method?): Boolean {
        return method != null && method.name == "toString" && method.parameterTypes.size == 0
    }

    /**
     * Determine whether the given method is originally declared by [Object].
     */
    fun isObjectMethod(method: Method?): Boolean {
        if (method == null) {
            return false
        }
        try {
            Any::class.java.getDeclaredMethod(method.name, *method.parameterTypes)
            return true
        } catch (ex: Exception) {
            return false
        }

    }

    /**
     * Determine whether the given method is a CGLIB 'renamed' method,
     * following the pattern "CGLIB$methodName$0".
     *
     * @param renamedMethod the method to check
     * @see //org.springframework.cglib.proxy.Enhancer.rename
     */
    fun isCglibRenamedMethod(renamedMethod: Method): Boolean {
        return CGLIB_RENAMED_METHOD_PATTERN.matcher(renamedMethod.name).matches()
    }

    /**
     * Make the given field accessible, explicitly setting it accessible if
     * necessary. The `setAccessible(true)` method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     *
     * @param field the field to make accessible
     * @see Field.setAccessible
     */
    fun makeAccessible(field: Field) {
        if ((!Modifier.isPublic(field.modifiers) || !Modifier.isPublic(field.declaringClass.modifiers) ||
                    Modifier.isFinal(field.modifiers)) && !field.isAccessible
        ) {
            field.isAccessible = true
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The `setAccessible(true)` method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     *
     * @param method the method to make accessible
     * @see Method.setAccessible
     */
    fun makeAccessible(method: Method) {
        if ((!Modifier.isPublic(method.modifiers) || !Modifier.isPublic(method.declaringClass.modifiers)) && !method.isAccessible) {
            method.isAccessible = true
        }
    }

    /**
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The `setAccessible(true)` method is only called
     * when actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     *
     * @param ctor the constructor to make accessible
     * @see Constructor.setAccessible
     */
    fun makeAccessible(ctor: Constructor<*>) {
        if ((!Modifier.isPublic(ctor.modifiers) || !Modifier.isPublic(ctor.declaringClass.modifiers)) && !ctor.isAccessible) {
            ctor.isAccessible = true
        }
    }

    /**
     * Perform the given callback operation on all matching methods of the given
     * class and superclasses (or given interface and super-interfaces).
     *
     * The same named method occurring on subclass and superclass will appear
     * twice, unless excluded by the specified [MethodFilter].
     *
     * @param clazz class to start looking at
     * @param mc    the callback to invoke for each method
     * @param mf    the filter that determines the methods to apply the callback to
     */
    @Throws(IllegalArgumentException::class)
    @JvmOverloads
    fun doWithMethods(clazz: Class<*>, mc: MethodCallback, mf: MethodFilter? = null) {
        // Keep backing up the inheritance hierarchy.
        val methods = clazz.declaredMethods
        for (method in methods) {
            if (mf != null && !mf.matches(method)) {
                continue
            }
            try {
                mc.doWith(method)
            } catch (ex: IllegalAccessException) {
                throw IllegalStateException(
                    "Shouldn't be illegal to access method '" + method.name
                            + "': " + ex
                )
            }
        }
        if (clazz.superclass != null) {
            doWithMethods(clazz.superclass!!, mc, mf)
        } else if (clazz.isInterface) {
            for (superIfc in clazz.interfaces) {
                doWithMethods(superIfc, mc, mf)
            }
        }
    }

    /**
     * Get all declared methods on the leaf class and all superclasses. Leaf
     * class methods are included first.
     */
    @Throws(IllegalArgumentException::class)
    fun getAllDeclaredMethods(leafClass: Class<*>): Array<Method> {
        val methods = ArrayList<Method>(32)
        doWithMethods(leafClass, object : MethodCallback {
            override fun doWith(method: Method) {
                methods.add(method)
            }
        })
        return methods.toTypedArray()
    }

    /**
     * Get the unique set of declared methods on the leaf class and all superclasses. Leaf
     * class methods are included first and while traversing the superclass hierarchy any methods found
     * with signatures matching a method already included are filtered out.
     */
    @Throws(IllegalArgumentException::class)
    fun getUniqueDeclaredMethods(leafClass: Class<*>): Array<Method> {
        val methods = ArrayList<Method>(32)
        doWithMethods(leafClass, object : MethodCallback {
            override fun doWith(method: Method) {
                var knownSignature = false
                var methodBeingOverriddenWithCovariantReturnType: Method? = null
                for (existingMethod in methods) {
                    if (method.name == existingMethod.name && Arrays.equals(
                            method.parameterTypes,
                            existingMethod.parameterTypes
                        )
                    ) {
                        // Is this a covariant return type situation?
                        if (existingMethod.returnType != method.returnType && existingMethod.returnType.isAssignableFrom(
                                method.returnType
                            )
                        ) {
                            methodBeingOverriddenWithCovariantReturnType = existingMethod
                        } else {
                            knownSignature = true
                        }
                        break
                    }
                }
                if (methodBeingOverriddenWithCovariantReturnType != null) {
                    methods.remove(methodBeingOverriddenWithCovariantReturnType)
                }
                if (!knownSignature && !isCglibRenamedMethod(method)) {
                    methods.add(method)
                }
            }
        })
        return methods.toTypedArray()
    }

    /**
     * Invoke the given callback on all fields in the target class, going up the
     * class hierarchy to get all declared fields.
     *
     * @param clazz the target class to analyze
     * @param fc    the callback to invoke for each field
     * @param ff    the filter that determines the fields to apply the callback to
     */
    @Throws(IllegalArgumentException::class)
    @JvmOverloads
    fun doWithFields(clazz: Class<*>, fc: FieldCallback, ff: FieldFilter? = null) {

        // Keep backing up the inheritance hierarchy.
        var targetClass: Class<*>? = clazz
        do {
            val fields = targetClass!!.declaredFields
            for (field in fields) {
                // Skip static and final fields.
                if (ff != null && !ff.matches(field)) {
                    continue
                }
                try {
                    fc.doWith(field)
                } catch (ex: IllegalAccessException) {
                    throw IllegalStateException(
                        "Shouldn't be illegal to access field '" + field.name + "': " + ex
                    )
                }

            }
            targetClass = targetClass.superclass
        } while (targetClass != null && targetClass != Any::class.java)
    }

    /**
     * Given the source object and the destination, which must be the same class
     * or a subclass, copy all fields, including inherited fields. Designed to
     * work on objects with public no-arg constructors.
     *
     * @throws IllegalArgumentException if the arguments are incompatible
     */
    @Throws(IllegalArgumentException::class)
    fun shallowCopyFieldState(src: Any?, dest: Any?) {
        requireNotNull(src) { "Source for field copy cannot be null" }
        requireNotNull(dest) { "Destination for field copy cannot be null" }
        require(src.javaClass.isAssignableFrom(dest.javaClass)) {
            ("Destination class [" + dest.javaClass.name
                    + "] must be same or subclass as source class [" + src.javaClass.name + "]")
        }
        doWithFields(src.javaClass, object : FieldCallback {
            @Throws(IllegalArgumentException::class, IllegalAccessException::class)
            override fun doWith(field: Field) {
                makeAccessible(field)
                val srcValue = field.get(src)
                field.set(dest, srcValue)
            }
        }, COPYABLE_FIELDS)
    }


    /**
     * Action to take on each method.
     */
    interface MethodCallback {

        /**
         * Perform an operation using the given method.
         *
         * @param method the method to operate on
         */
        @Throws(IllegalArgumentException::class, IllegalAccessException::class)
        fun doWith(method: Method)
    }


    /**
     * Callback optionally used to filter methods to be operated on by a method callback.
     */
    interface MethodFilter {

        /**
         * Determine whether the given method matches.
         *
         * @param method the method to check
         */
        fun matches(method: Method): Boolean
    }


    /**
     * Callback interface invoked on each field in the hierarchy.
     */
    interface FieldCallback {

        /**
         * Perform an operation using the given field.
         *
         * @param field the field to operate on
         */
        @Throws(IllegalArgumentException::class, IllegalAccessException::class)
        fun doWith(field: Field)
    }


    /**
     * Callback optionally used to filter fields to be operated on by a field callback.
     */
    interface FieldFilter {

        /**
         * Determine whether the given field matches.
         *
         * @param field the field to check
         */
        fun matches(field: Field): Boolean
    }

    fun <T> newInstance(
        cls: Class<T>,
        parameterTypes: Array<Class<*>?>,
        parameters: Array<Any?>
    ): T {
        return try {
            val constructor = cls.getConstructor(*parameterTypes)
            constructor.newInstance(*parameters)
        } catch (e: IllegalAccessException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InstantiationException) {
            throw java.lang.RuntimeException(e)
        } catch (e: NoSuchMethodException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw java.lang.RuntimeException(e)
        }
    }

    fun <T> newInstance(cls: Class<T>): T {
        return try {
            cls.newInstance()
        } catch (e: IllegalAccessException) {
            throw java.lang.RuntimeException(e)
        } catch (e: InstantiationException) {
            throw java.lang.RuntimeException(e)
        }
    }
}
