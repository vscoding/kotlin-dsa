package io.intellij.dsa.tree

import io.intellij.dsa.tree.trie.TrieUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * TrieTest
 *
 * @author dev@intellij.io
 */
class TrieTest {

  @Test
  fun `domain trie supports exact and suffix matching`() {
    val cl = Thread.currentThread().contextClassLoader
    val input = cl.getResourceAsStream("trie/domain_apple.txt") ?: error("resource not found")
    val domainSegmentFunc = TrieUtils.segmentFunTpl("domain")

    domainSegmentFunc("www.apple.com").forEach {
      println(it)
    }

    val trie = input.use { TrieUtils.buildTrieFromTxtFile(it, domainSegmentFunc) }

    println("trie size: ${trie.size()}")

    assertTrue(trie.size() > 0)
    assertTrue(trie.contains("www.apple.com"))
    assertFalse(trie.contains("www.google.com"))

    assertTrue(
      trie.containsPartial("a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q.r.s.t.u.v.w.x.y.z.www.apple.com"),
    )

    assertFalse(
      trie.containsPartial("a.b.c.d.e.f.g.h.i.j.k.l.m.n.o.p.q.r.s.t.u.v.w.x.y.z.apple.com"),
    )
  }

}
