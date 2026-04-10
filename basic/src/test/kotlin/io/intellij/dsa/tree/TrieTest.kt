package io.intellij.dsa.tree

import io.intellij.dsa.tree.trie.TrieUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * TrieTest
 *
 * @author tech@intellij.io
 */
class TrieTest {

  @Test
  fun `test string trie`() {
    val cl = Thread.currentThread().contextClassLoader
    val input = cl.getResourceAsStream("trie/domain_apple.txt") ?: error("resource not found")
    val domainSegmentFunc = TrieUtils.segmentFunTpl("domain")

    domainSegmentFunc("www.apple.com").forEach {
      println(it)
    }

    val trie = TrieUtils.buildTrieFromTxtFile(input, domainSegmentFunc)

    println("trie size: ${trie.size()}")

    Assertions.assertTrue(trie.contains("www.apple.com"))
    Assertions.assertFalse(trie.contains("www.google.com"))

    Assertions.assertTrue(
      trie.containsPartial("a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q.r.s.t.u.v.w.x.y.z.www.apple.com"),
    )

    Assertions.assertFalse(
      trie.containsPartial("a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q.r.s.t.u.v.w.x.y.z.apple.com"),
    )
  }

}