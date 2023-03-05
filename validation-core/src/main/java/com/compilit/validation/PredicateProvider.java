package com.compilit.validation;

import com.compilit.validation.api.DecimalNumberPredicateProvider;
import com.compilit.validation.api.NumberPredicateProvider;
import com.compilit.validation.api.ObjectPredicateProvider;

class PredicateProvider<T>
  implements NumberPredicateProvider, DecimalNumberPredicateProvider, ObjectPredicateProvider<T> {
}
