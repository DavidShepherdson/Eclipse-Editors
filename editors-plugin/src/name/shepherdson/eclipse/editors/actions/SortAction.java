package name.shepherdson.eclipse.editors.actions;

import org.eclipse.jface.action.Action;

import name.shepherdson.eclipse.editors.models.editor.EditorComparator.SortType;
import name.shepherdson.eclipse.editors.views.editors.EditorTableView;

public class SortAction extends Action {
	protected EditorTableView editorTableView;
	protected SortType sortType;

	public SortAction(EditorTableView editorTableView, SortType sortType, String text, String tooltip) {
		this.editorTableView = editorTableView;
		this.sortType = sortType;
		setText(text);
		setToolTipText(tooltip);
	}

	@Override
	public void run() {
		editorTableView.getSorter().setSortBy(sortType);
		editorTableView.refresh();
	}

}
