package com.compilit.validation.api;

import com.compilit.validation.predicates.DecimalNumberPredicateProvider;
import com.compilit.validation.predicates.NumberPredicateProvider;
import com.compilit.validation.predicates.ObjectPredicateProvider;

class PredicateProvider<T> implements NumberPredicateProvider, DecimalNumberPredicateProvider, ObjectPredicateProvider<T> {
}
