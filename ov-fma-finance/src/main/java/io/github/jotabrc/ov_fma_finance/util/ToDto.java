package io.github.jotabrc.ov_fma_finance.util;

@FunctionalInterface
public interface ToDto<R> {
    R transform();
}
