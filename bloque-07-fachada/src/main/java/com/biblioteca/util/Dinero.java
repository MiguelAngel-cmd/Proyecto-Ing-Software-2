package com.biblioteca.util;

/** Da formato de pesos colombianos: 12500 -> "$12.500". */
public final class Dinero {

    private Dinero() {
    }

    public static String formato(long pesos) {
        String digitos = Long.toString(Math.abs(pesos));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digitos.length(); i++) {
            if (i > 0 && (digitos.length() - i) % 3 == 0) {
                sb.append('.');
            }
            sb.append(digitos.charAt(i));
        }
        return (pesos < 0 ? "-$" : "$") + sb;
    }
}
