
package name.shepherdson.eclipse.editors.models.editor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class EditorComparator extends ViewerComparator
{
    public enum SortType
    {
        ACCESS,
        NATURAL,
        NAME,
        PATH
    }

    private SortType sortBy = SortType.PATH;

    public EditorComparator(SortType sortBy)
    {
        this.sortBy = sortBy;
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {
        IEditor editor1 = (IEditor) e1;
        IEditor editor2 = (IEditor) e2;

        switch (sortBy)
        {
            case ACCESS:
                return compare(editor2.getHistoryPosition(), editor1.getHistoryPosition());
            case NATURAL:
                return compare(editor1.getNaturalPosition(), editor2.getNaturalPosition());
            case NAME:
                return compare(editor1.getName().toLowerCase(), editor2.getName().toLowerCase());
            case PATH:
                return compare(
                        editor1.getFilePath().toLowerCase(),
                        editor2.getFilePath().toLowerCase());
            default:
                return 0;
        }
    }

    @SuppressWarnings(
    {
        "rawtypes", "unchecked"
    })
    public int compare(final Comparable one, final Comparable two)
    {
        if (one == null ^ two == null)
        {
            return (one == null) ? -1 : 1;
        }

        if (one == null && two == null)
        {
            return 0;
        }

        return one.compareTo(two);
    }

    public SortType getSortBy()
    {
        return sortBy;
    }

    public void setSortBy(SortType sortBy)
    {
        this.sortBy = sortBy;
    }
}
