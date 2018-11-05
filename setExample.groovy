[Groovy Set Examples - Grails Cookbook](http://grails.asia/groovy-set-examples)

class Test {
	static main(args) {
		Set stringSet = ["apple", "apple", "banana", "banana"]
		println "size = ${stringSet.size()}"
		stringSet.each {
			println it
		}
	}
}

Even when there are duplicates in the given collection, only unique elements will be stored. Hence, the output will be:

size = 2
banana
apple

grep - We can also use grep by using any logical expression just like in findall. For example:

class Test {
	static main(args) {
		Set mySet = ["apple", "banana", "carrots"]
		println mySet.grep{it.contains("e")}
	}
}

The result is:

[apple]

findAll - this can be used to filter the items on a list based on some criteria. Below is an example:

class Test {
	static main(args) {
		Set mySet = ["apple", "banana", "carrots"]
		println mySet.findAll {it.contains("s")}
	}
}

And the output is the subset of the list that has an "s" in it's letters:

[carrots]
