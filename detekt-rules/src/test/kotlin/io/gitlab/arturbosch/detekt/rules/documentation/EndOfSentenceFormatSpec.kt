package io.gitlab.arturbosch.detekt.rules.documentation

import io.gitlab.arturbosch.detekt.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.subject.SubjectSpek

/**
 * @author Marvin Ramin
 */
class EndOfSentenceFormatSpec : SubjectSpek<KDocStyle>({
	subject { KDocStyle() }

	it("reports invalid KDoc endings on classes") {
		val code = """
			/** Some doc */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).hasSize(1)
	}

	it("reports invalid KDoc endings on objects") {
		val code = """
			/** Some doc */
			object Test {
			}
			"""
		assertThat(subject.lint(code)).hasSize(1)
	}

	it("reports invalid KDoc endings on properties") {
		val code = """
			class Test {
			 	/** Some doc */
				val test = 3
			}
			"""
		assertThat(subject.lint(code)).hasSize(1)
	}

	it("reports invalid KDoc endings on top-level functions") {
		val code = """
			/** Some doc */
			fun test() = 3
			"""
		assertThat(subject.lint(code)).hasSize(1)
	}

	it("reports invalid KDoc endings on functions") {
		val code = """
			class Test {
			 	/** Some doc */
				fun test() = 3
			}
			"""
		assertThat(subject.lint(code)).hasSize(1)
	}

	it("reports invalid KDoc endings") {
		val code = """
			class Test {
			 	/** Some doc-- */
				fun test() = 3
			}
			"""
		assertThat(subject.lint(code)).hasSize(1)
	}

	it("reports invalid KDoc endings in block") {
		val code = """
			/**
			 * Something off abc@@
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).hasSize(1)
	}

	it("does not validate first sentence KDoc endings in a multi sentence comment") {
		val code = """
			/**
			 * This sentence is correct.
			 *
			 * This sentence doesn't matter
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}

	it("does not report KDoc which doesn't contain any real sentence") {
		val code = """
			/**
			 * @author Someone
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}


	it("does not report KDoc which doesn't contain any real sentence but many tags") {
		val code = """
			/**
			 * @configuration this - just an example (default: 150)
			 *
			 * @active since v1.0.0
			 * @author person1
			 * @author person2
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}


	it("does not report KDoc which doesn't contain any real sentence but html tags") {
		val code = """
			/**
			 *
			 * <noncompliant>
			 * fun foo(): Unit { }
			 * </noncompliant>
			 *
			 * <compliant>
			 * fun foo() { }
			 * </compliant>
			 *
			 * @author someone
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}

	it("does not report KDoc ending with periods") {
		val code = """
			/**
			 * Something correct.
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}

	it("does not report KDoc ending with questionmarks") {
		val code = """
			/**
			 * Something correct?
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}

	it("does not report KDoc ending with exclamation marks") {
		val code = """
			/**
			 * Something correct!
			 */
			class Test {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}

	it("does not report URLs in comments") {
		val code = """
			/** http://www.google.com */
			class Test1 {
			}

			/** Look here
			www.google.com */
			class Test2 {
			}
			"""
		assertThat(subject.lint(code)).isEmpty()
	}
})