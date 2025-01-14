package org.spc.impl;


import org.spc.tool.Constants;
import org.spc.tool.Wrapper;

import java.util.Objects;

import static org.spc.tool.Toolkit.comparableClassFor;
import static org.spc.tool.Toolkit.compareComparables;

/**
 * HamaTreeNode
 * <p>
 * Hamamap的树节点
 */
public class HamaTreeNode<K, V> extends HamaNode<K, V> {

    /**
     * The parent of this node,  red-black tree links
     * <p>
     * 该节点的父节点, 红黑树链接
     */
    HamaTreeNode<K, V> parent;
    /**
     * The left child of this node, or null if none
     * <p>
     * 该节点的左子节点，如果没有则为null
     */
    HamaTreeNode<K, V> left;
    /**
     * The right child of this node, or null if none
     * <p>
     * 该节点的右子节点，如果没有则为null
     */
    HamaTreeNode<K, V> right;
    /**
     * The predecessor of this node in the traversal order
     * <p>
     * 该节点在遍历顺序中的前驱节点
     */
    HamaTreeNode<K, V> prev;

    /**
     * True if this node is red
     * <p>
     * 如果该节点是红色，则为true
     */
    boolean red;


    HamaTreeNode(int hash, K key, V val, HamaNode<K, V> next) {
        super(hash, key, val, next);
    }

    /**
     * Ensures that the given root is the first node of its bin
     * <p>
     * 确保给定的根节点是其bin的第一个节点
     */
    static <K, V> void moveRootToFront(HamaNode<K, V>[] tab, HamaTreeNode<K, V> root) {
        int n;
        if (root != null && tab != null && (n = tab.length) > 0) {
            int index = (n - 1) & root.hash;
            HamaTreeNode<K, V> first = (HamaTreeNode<K, V>) tab[index];

            if (root != first) {
                HamaNode<K, V> rn;
                tab[index] = root;
                HamaTreeNode<K, V> rp = root.prev;
                if ((rn = root.next) != null) {
                    ((HamaTreeNode<K, V>) rn).prev = rp;
                }
                if (rp != null) {
                    rp.next = rn;
                }
                if (first != null) {
                    first.prev = root;
                }
                root.next = first;
                root.prev = null;
            }
            assert checkInvariants(root);
        }
    }

    /**
     * 当哈希码相等且不可比较时，用于排序插入的打破平局的实用程序
     */
    static int tieBreakOrder(Object a, Object b) {
        int d;
        if (a == null || b == null || (d = a.getClass().getName().compareTo(b.getClass().getName())) == 0) d = (System.identityHashCode(a) <= System.identityHashCode(b) ? -1 : 1);
        return d;
    }


    /**
     * rotateLeft, Returns the root of a tree with the given head
     * <p>
     * 左旋, 返回具有给定头的树的根
     */
    static <K, V> HamaTreeNode<K, V> rotateLeft(HamaTreeNode<K, V> root, HamaTreeNode<K, V> p) {
        HamaTreeNode<K, V> r, pp, rl;
        if (p != null && (r = p.right) != null) {
            if ((rl = p.right = r.left) != null) rl.parent = p;
            if ((pp = r.parent = p.parent) == null) (root = r).red = false;
            else if (pp.left == p) pp.left = r;
            else pp.right = r;
            r.left = p;
            p.parent = r;
        }
        return root;
    }

    /**
     * rotateRight, Returns the root of a tree with the given head
     * <p>
     * 右旋, 返回具有给定头的树的根
     */
    static <K, V> HamaTreeNode<K, V> rotateRight(HamaTreeNode<K, V> root, HamaTreeNode<K, V> p) {
        HamaTreeNode<K, V> l, pp, lr;

        if (p != null && (l = p.left) != null) {
            if ((lr = p.left = l.right) != null) {
                lr.parent = p;
            }
            if ((pp = l.parent = p.parent) == null) {
                (root = l).red = false;
            } else if (pp.right == p) {
                pp.right = l;
            } else {
                pp.left = l;
            }
            l.right = p;
            p.parent = l;
        }
        return root;
    }

    /**
     * Returns the root of a tree with the given node inserted
     * <p>
     * 平衡插入, 返回具有给定节点插入的树的根
     */
    static <K, V> HamaTreeNode<K, V> balanceInsertion(HamaTreeNode<K, V> root, HamaTreeNode<K, V> x) {
        x.red = true;

        for (HamaTreeNode<K, V> xp, xpp, xppl, xppr; ; ) {

            if ((xp = x.parent) == null) {
                x.red = false;
                return x;
            } else if (!xp.red || (xpp = xp.parent) == null) {
                return root;
            }

            if (xp == (xppl = xpp.left)) {
                if ((xppr = xpp.right) != null && xppr.red) {
                    xppr.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x = xpp;
                } else {
                    if (x == xp.right) {
                        root = rotateLeft(root, x = xp);
                        xpp = (xp = x.parent) == null ? null : xp.parent;
                    }
                    if (xp != null) {
                        xp.red = false;
                        if (xpp != null) {
                            xpp.red = true;
                            root = rotateRight(root, xpp);
                        }
                    }
                }
            } else {
                if (xppl != null && xppl.red) {
                    xppl.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x = xpp;
                } else {
                    if (x == xp.left) {
                        root = rotateRight(root, x = xp);
                        xpp = (xp = x.parent) == null ? null : xp.parent;
                    }
                    if (xp != null) {
                        xp.red = false;
                        if (xpp != null) {
                            xpp.red = true;
                            root = rotateLeft(root, xpp);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the root of a tree with the given node deleted
     * <p>
     * 平衡删除, 返回具有给定节点删除的树的根
     */
    static <K, V> HamaTreeNode<K, V> balanceDeletion(HamaTreeNode<K, V> root, HamaTreeNode<K, V> x) {

        for (HamaTreeNode<K, V> xp, xpl, xpr; ; ) {

            if (x == null || x == root) {
                return root;
            } else if ((xp = x.parent) == null) {
                x.red = false;
                return x;
            } else if (x.red) {
                x.red = false;
                return root;
            } else if ((xpl = xp.left) == x) {

                if ((xpr = xp.right) != null && xpr.red) {
                    xpr.red = false;
                    xp.red = true;
                    root = rotateLeft(root, xp);
                    xpr = (xp = x.parent) == null ? null : xp.right;
                }

                if (xpr == null) {
                    x = xp;
                } else {
                    HamaTreeNode<K, V> sl = xpr.left, sr = xpr.right;
                    if ((sr == null || !sr.red) && (sl == null || !sl.red)) {
                        xpr.red = true;
                        x = xp;
                    } else {
                        if (sr == null || !sr.red) {
                            sl.red = false;
                            xpr.red = true;
                            root = rotateRight(root, xpr);
                            xpr = (xp = x.parent) == null ? null : xp.right;
                        }
                        if (xpr != null) {
                            xpr.red = xp.red;
                            if ((sr = xpr.right) != null) sr.red = false;
                        }
                        if (xp != null) {
                            xp.red = false;
                            root = rotateLeft(root, xp);
                        }
                        x = root;
                    }
                }

            } else { // symmetric 与上面对称

                if (xpl != null && xpl.red) {
                    xpl.red = false;
                    xp.red = true;
                    root = rotateRight(root, xp);
                    xpl = (xp = x.parent) == null ? null : xp.left;
                }

                if (xpl == null) {
                    x = xp;
                } else {
                    HamaTreeNode<K, V> sl = xpl.left, sr = xpl.right;
                    if ((sl == null || !sl.red) && (sr == null || !sr.red)) {
                        xpl.red = true;
                        x = xp;
                    } else {

                        if (sl == null || !sl.red) {
                            sr.red = false;
                            xpl.red = true;
                            root = rotateLeft(root, xpl);
                            xpl = (xp = x.parent) == null ? null : xp.left;
                        }

                        if (xpl != null) {
                            xpl.red = xp.red;
                            if ((sl = xpl.left) != null) sl.red = false;
                        }

                        if (xp != null) {
                            xp.red = false;
                            root = rotateRight(root, xp);
                        }
                        x = root;
                    }
                }
            }
        }
    }


    /**
     * Recursive invariant check
     * <p>
     * 递归不变性检查
     */
    static <K, V> boolean checkInvariants(HamaTreeNode<K, V> t) {
        HamaTreeNode<K, V> tp = t.parent, tl = t.left, tr = t.right, tb = t.prev, tn = (HamaTreeNode<K, V>) t.next;
        if (tb != null && tb.next != t) {
            return false;
        }
        if (tn != null && tn.prev != t) {
            return false;
        }
        if (tp != null && t != tp.left && t != tp.right) {
            return false;
        }
        if (tl != null && (tl.parent != t || tl.hash > t.hash)) {
            return false;
        }
        if (tr != null && (tr.parent != t || tr.hash < t.hash)) {
            return false;
        }
        if (t.red && tl != null && tl.red && tr != null && tr.red) {
            return false;
        }
        if (tl != null && !checkInvariants(tl)) {
            return false;
        }
        return tr == null || checkInvariants(tr);
    }


    /**
     * Returns root of tree containing this node
     * <p>
     * 返回包含此节点的树的根
     */
    final HamaTreeNode<K, V> root() {
        for (HamaTreeNode<K, V> r = this, p; ; ) {
            if ((p = r.parent) == null) {
                return r;
            }
            r = p;
        }
    }


    /**
     * Finds the node starting at root p with the given hash and key.
     * The kc argument caches comparableClassFor(key) upon first use
     * comparing keys.
     * <p>
     * 从根节点p开始查找具有给定哈希和键的节点
     */
    final HamaTreeNode<K, V> find(int h, Object k, Class<?> kc) {

        HamaTreeNode<K, V> p = this;

        do {
            int ph, dir;
            K pk;
            HamaTreeNode<K, V> pl = p.left, pr = p.right, q;

            if ((ph = p.hash) > h) {
                p = pl;
            } else if (ph < h) {
                p = pr;
            } else if ((pk = p.key) == k || (k != null && k.equals(pk))) {
                return p;
            } else if (pl == null) {
                p = pr;
            } else if (pr == null) {
                p = pl;
            } else if ((kc != null || (kc = comparableClassFor(k)) != null) && (dir = compareComparables(kc, k, pk)) != 0) {
                p = (dir < 0) ? pl : pr;
            } else if ((q = pr.find(h, k, kc)) != null) {
                return q;
            } else p = pl;
        } while (p != null);

        return null;
    }

    /**
     * Calls find for root node
     * <p>
     * 调用根节点
     */
    final Wrapper<K, V> getTreeNode(int h, Object k) {
        return ((parent != null) ? root() : this).find(h, k, null);
    }



    /* ------------------------------------------------------------ */
    // Red-black tree methods, all adapted from CLR  红黑树方法，全部改编自CLR

    /**
     * Forms tree of the nodes linked from this node
     * <p>
     * 从该节点链接的节点形成树
     */
    final void treeify(HamaNode<K, V>[] tab) {
        HamaTreeNode<K, V> root = null;

        for (HamaTreeNode<K, V> x = this, next; x != null; x = next) {
            next = (HamaTreeNode<K, V>) x.next;
            x.left = x.right = null;

            if (root == null) {
                x.parent = null;
                x.red = false;
                root = x;
            } else {
                K k = x.key;
                int h = x.hash;
                Class<?> kc = null;

                for (HamaTreeNode<K, V> p = root; ; ) {
                    int dir, ph;
                    K pk = p.key;

                    if ((ph = p.hash) > h) dir = -1;
                    else if (ph < h) dir = 1;
                    else if ((kc == null && (kc = comparableClassFor(k)) == null) || (dir = compareComparables(kc, k, pk)) == 0) dir = tieBreakOrder(k, pk);

                    HamaTreeNode<K, V> xp = p;
                    if ((p = (dir <= 0) ? p.left : p.right) == null) {
                        x.parent = xp;
                        if (dir <= 0) xp.left = x;
                        else xp.right = x;
                        root = balanceInsertion(root, x);
                        break;
                    }
                }
            }
        }
        moveRootToFront(tab, root);
    }

    /**
     * Returns a list of non-TreeNodes replacing those linked fromthis node
     * <p>
     * 返回替换从该节点链接的非TreeNodes的列表
     */
    final HamaNode<K, V> untreeify(Hamamap<K, V> map) {
        HamaNode<K, V> hd = null, tl = null;

        for (HamaNode<K, V> q = this; q != null; q = q.next) {
            HamaNode<K, V> p = map.replacementNode(q, null);
            if (tl == null) hd = p;
            else tl.next = p;
            tl = p;
        }
        return hd;
    }


    /**
     * Tree version of putVal
     * <p>
     * putVal的树版本
     */
    final HamaTreeNode<K, V> putTreeVal(Hamamap<K, V> map, HamaNode<K, V>[] tab, int h, K k, V v) {
        Class<?> kc = null;
        boolean searched = false;
        HamaTreeNode<K, V> root = (parent != null) ? root() : this;

        for (HamaTreeNode<K, V> p = root; ; ) {
            int dir, ph;
            K pk;
            if ((ph = p.hash) > h) dir = -1;
            else if (ph < h) dir = 1;
            else if ((pk = p.key) == k || (k != null && k.equals(pk))) return p;
            else if ((kc == null && (kc = comparableClassFor(k)) == null) || (dir = compareComparables(kc, k, pk)) == 0) {
                if (!searched) {
                    HamaTreeNode<K, V> q, ch;
                    searched = true;
                    if (((ch = p.left) != null && (q = ch.find(h, k, kc)) != null) || ((ch = p.right) != null && (q = ch.find(h, k, kc)) != null)) return q;
                }
                dir = tieBreakOrder(k, pk);
            }

            HamaTreeNode<K, V> xp = p;
            if ((p = (dir <= 0) ? p.left : p.right) == null) {
                HamaNode<K, V> xpn = xp.next;
                HamaTreeNode<K, V> x = map.newTreeNode(h, k, v, xpn);
                if (dir <= 0) xp.left = x;
                else xp.right = x;
                xp.next = x;
                x.parent = x.prev = xp;
                if (xpn != null) ((HamaTreeNode<K, V>) xpn).prev = x;
                moveRootToFront(tab, balanceInsertion(root, x));
                return null;
            }
        }
    }


    /**
     * Removes the given node, that must be present before this call.
     * This is messier than typical red-black deletion code because we
     * cannot swap the contents of an interior node with a leaf
     * successor that is pinned by "next" pointers that are accessible
     * independently during traversal. So instead we swap the tree
     * linkages. If the current tree appears to have too few nodes,
     * the bin is converted back to a plain bin. (The test triggers
     * somewhere between 2 and 6 nodes, depending on tree structure)
     * <p>
     * 删除给定节点，该节点在此调用之前必须存在
     */
    final void removeTreeNode(Hamamap<K, V> map, HamaNode<K, V>[] tab, boolean movable) {
        int n;
        if (tab == null || (n = tab.length) == 0) return;
        int index = (n - 1) & hash;
        HamaTreeNode<K, V> first = (HamaTreeNode<K, V>) tab[index], root = first, rl;
        HamaTreeNode<K, V> succ = (HamaTreeNode<K, V>) next, pred = prev;

        if (pred == null) tab[index] = first = succ;
        else pred.next = succ;
        if (succ != null) succ.prev = pred;
        if (first == null) return;
        if (root.parent != null) root = root.root();
        if (movable && (root.right == null || (rl = root.left) == null || rl.left == null)) {
            tab[index] = first.untreeify(map);  // too small
            return;
        }

        HamaTreeNode<K, V> p = this, pl = left, pr = right, replacement;
        if (pl != null && pr != null) {
            HamaTreeNode<K, V> s = pr, sl;
            while ((sl = s.left) != null) // find successor
                s = sl;
            boolean c = s.red;
            s.red = p.red;
            p.red = c; // swap colors
            HamaTreeNode<K, V> sr = s.right;
            HamaTreeNode<K, V> pp = p.parent;
            if (s == pr) { // p was s's direct parent
                p.parent = s;
                s.right = p;
            } else {
                HamaTreeNode<K, V> sp = s.parent;
                if ((p.parent = sp) != null) {
                    if (s == sp.left) sp.left = p;
                    else sp.right = p;
                }

                pr.parent = s;
            }
            p.left = null;
            if ((p.right = sr) != null) sr.parent = p;
            pl.parent = s;
            if ((s.parent = pp) == null) root = s;
            else if (p == pp.left) pp.left = s;
            else pp.right = s;
            replacement = Objects.requireNonNullElse(sr, p);
        } else if (pl != null) replacement = pl;
        else replacement = Objects.requireNonNullElse(pr, p);
        if (replacement != p) {
            HamaTreeNode<K, V> pp = replacement.parent = p.parent;
            if (pp == null) (root = replacement).red = false;
            else if (p == pp.left) pp.left = replacement;
            else pp.right = replacement;
            p.left = p.right = p.parent = null;
        }

        HamaTreeNode<K, V> r = p.red ? root : balanceDeletion(root, replacement);

        if (replacement == p) {  // detach
            HamaTreeNode<K, V> pp = p.parent;
            p.parent = null;
            if (pp != null) {
                if (p == pp.left) pp.left = null;
                else if (p == pp.right) pp.right = null;
            }
        }
        if (movable) moveRootToFront(tab, r);
    }

    /**
     * Splits nodes in a tree bin into lower and upper tree bins,
     * or untreeifies if now too small. Called only from resize;
     * see above discussion about split bits and indices.
     * <p>
     * 将树bin中的节点拆分为较低和较高的树bin，如果现在太小，则取消树化。 仅从调整大小调用; 请参见上面关于拆分位和索引的讨论
     *
     * @param map   the map
     * @param tab   the table for recording bin heads
     * @param index the index of the table being split
     * @param bit   the bit of hash to split on
     */
    final void split(Hamamap<K, V> map, HamaNode<K, V>[] tab, int index, int bit) {
        HamaTreeNode<K, V> b = this;
        // Relink into lo and hi lists, preserving order
        HamaTreeNode<K, V> loHead = null, loTail = null;
        HamaTreeNode<K, V> hiHead = null, hiTail = null;
        int lc = 0, hc = 0;
        for (HamaTreeNode<K, V> e = b, next; e != null; e = next) {
            next = (HamaTreeNode<K, V>) e.next;
            e.next = null;
            if ((e.hash & bit) == 0) {
                if ((e.prev = loTail) == null) loHead = e;
                else loTail.next = e;
                loTail = e;
                ++lc;
            } else {
                if ((e.prev = hiTail) == null) hiHead = e;
                else hiTail.next = e;
                hiTail = e;
                ++hc;
            }
        }

        if (loHead != null) {
            if (lc <= Constants.UNTREEIFY_THRESHOLD) tab[index] = loHead.untreeify(map);
            else {
                tab[index] = loHead;
                if (hiHead != null) // (else is already treeified)
                    loHead.treeify(tab);
            }
        }
        if (hiHead != null) {
            if (hc <= Constants.UNTREEIFY_THRESHOLD) tab[index + bit] = hiHead.untreeify(map);
            else {
                tab[index + bit] = hiHead;
                if (loHead != null) hiHead.treeify(tab);
            }
        }
    }
}


