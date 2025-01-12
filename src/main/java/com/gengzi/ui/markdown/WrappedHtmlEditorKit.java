//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.gengzi.ui.markdown;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.InlineView;
import javax.swing.text.html.ListView;

public class WrappedHtmlEditorKit extends HTMLEditorKit {
    private static final long serialVersionUID = 1L;
    private ViewFactory viewFactory = null;

    public WrappedHtmlEditorKit() {
        this.viewFactory = new WrappedHtmlFactory();
    }

    public ViewFactory getViewFactory() {
        return this.viewFactory;
    }

    private class WrappedHtmlFactory extends HTMLEditorKit.HTMLFactory {
        private static final int MIN_HEIGHT_VIEWS = 23;

        private WrappedHtmlFactory() {
        }

        public View create(Element e) {
            View v = super.create(e);
            if (v instanceof InlineView && !v.getClass().getSimpleName().equals("BRView")) {
                View v2 = new InlineView(e) {
                    public float getMaximumSpan(int axis) {
                        float result = super.getMaximumSpan(axis);
                        if (axis == 1) {
                            result = Math.max(result, 23.0F);
                        }

                        return result;
                    }

                    public float getMinimumSpan(int axis) {
                        float result = super.getMinimumSpan(axis);
                        if (axis == 1) {
                            result = Math.max(result, 23.0F);
                        }

                        return result;
                    }

                    public float getPreferredSpan(int axis) {
                        float result = super.getPreferredSpan(axis);
                        if (axis == 1) {
                            result = Math.max(result, 23.0F);
                        }

                        return result;
                    }

                    public float getAlignment(int axis) {
                        if (axis == 1) {
                            super.getAlignment(axis);
                            return 0.5F;
                        } else {
                            return super.getAlignment(axis);
                        }
                    }
                };
                v = v2;
            } else if (v instanceof ListView) {
                View v2 = new ListView(e) {
                    public float getMaximumSpan(int axis) {
                        float result = super.getMaximumSpan(axis);
                        if (axis == 1) {
                            result = Math.max(result, 23.0F);
                        }

                        return result;
                    }

                    public float getMinimumSpan(int axis) {
                        float result = super.getMinimumSpan(axis);
                        if (axis == 1) {
                            result = Math.max(result, 23.0F);
                        }

                        return result;
                    }

                    public float getPreferredSpan(int axis) {
                        float result = super.getPreferredSpan(axis);
                        if (axis == 1) {
                            result = Math.max(result, 23.0F);
                            float total = 0.0F;

                            for(int i = 0; i < this.getViewCount(); ++i) {
                                View child = this.getView(i);
                                if (child instanceof InlineView) {
                                    total += child.getPreferredSpan(axis);
                                }
                            }

                            result = Math.max(result, total);
                        }

                        return result;
                    }
                };
                v = v2;
            }

            return v;
        }
    }
}
