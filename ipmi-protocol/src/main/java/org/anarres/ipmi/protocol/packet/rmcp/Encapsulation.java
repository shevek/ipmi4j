/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface Encapsulation {

    public static class Utils {

        @CheckForNull
        public static <T> T getEncapsulated(@Nonnull Class<T> type, @Nonnull Object value) {
            if (type.isInstance(value))
                return type.cast(value);
            if (value instanceof Encapsulation)
                return ((Encapsulation) value).getEncapsulated(type);
            return null;
        }
    }

    @CheckForNull
    public <T> T getEncapsulated(@Nonnull Class<T> type);
}
