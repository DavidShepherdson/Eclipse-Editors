
package name.shepherdson.eclipse.editors.models.settings;

import java.util.Map;
import java.util.TreeMap;

import name.shepherdson.eclipse.editors.models.editor.EditorComparator;
import name.shepherdson.eclipse.editors.models.editor.EditorComparator.SortType;

public class EditorSetSettingsModel
{
    private String name;
    private SortType sortBy = EditorComparator.SortType.ACCESS;

    private Map<String, EditorModel> editorModels = new TreeMap<>();

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public SortType getSortBy()
    {
        return sortBy;
    }

    public void setSortBy(SortType sortBy)
    {
        this.sortBy = sortBy;
    }

    public Map<String, EditorModel> getEditorModels()
    {
        return editorModels;
    }

    public void setEditorModels(Map<String, EditorModel> editorModels)
    {
        this.editorModels = editorModels;
    }

}
