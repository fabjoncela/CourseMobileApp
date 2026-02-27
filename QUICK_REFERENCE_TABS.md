# 🎯 QUICK REFERENCE - Tabbed Interface

## 📱 Assignments (3 Tabs)

### Tab 1: Pending
- **Shows**: Active assignments waiting to be completed
- **Actions**: 
  - ✅ Mark as Complete → Moves to "Completed"
  - 🗑️ Delete → Moves to "Deleted"
  - 🔍 Search pending assignments

### Tab 2: Completed
- **Shows**: Finished assignments
- **Actions**:
  - 🗑️ Delete → Moves to "Deleted"
  - 🔍 Search completed assignments

### Tab 3: Deleted
- **Shows**: Soft-deleted assignments (recoverable)
- **Actions**:
  - ♻️ Restore → Returns to "Pending"
  - ❌ Permanent Delete → Removes forever
  - 🔍 Search deleted assignments

---

## 📝 Notes (2 Tabs)

### Tab 1: Active
- **Shows**: Current notes you're using
- **Actions**:
  - 🗑️ Delete → Moves to "Deleted"
  - 🔍 Search active notes

### Tab 2: Deleted
- **Shows**: Soft-deleted notes (recoverable)
- **Actions**:
  - ♻️ Restore → Returns to "Active"
  - ❌ Permanent Delete → Removes forever
  - 🔍 Search deleted notes

---

## 🔄 Status Flow

### Assignments:
```
CREATE → Pending
         │
         ├→ Complete → Completed
         │              │
         └→ Delete  ←───┘
               ↓
            Deleted
               ↓
            Restore
               ↓
            Pending
```

### Notes:
```
CREATE → Active
         │
         └→ Delete
            ↓
         Deleted
            ↓
         Restore
            ↓
         Active
```

---

## ⌨️ Database Fields

### Assignment Status Values:
- `"pending"` - Active assignment
- `"completed"` - Finished assignment
- `"deleted"` - Soft-deleted (can restore)

### Note Status Values:
- `"active"` - Current note
- `"deleted"` - Soft-deleted (can restore)

---

## 🎨 UI Components

### TabLayout:
```java
TabLayout tabLayout = view.findViewById(R.id.assignmentTabLayout);
// or
TabLayout tabLayout = view.findViewById(R.id.noteTabLayout);
```

### Tab Colors:
- Background: `#1976D2` (Material Blue)
- Text: White
- Indicator: White

---

## 🔧 Key Methods

### AssignmentsFragment:
```java
loadAssignmentsByStatus()      // Load items for current tab
searchAssignments(query)       // Search within current tab
deleteAssignment(assignment)   // Soft delete (→ Deleted tab)
restoreAssignment(assignment)  // Restore (→ Pending tab)
permanentlyDeleteAssignment()  // Remove from DB forever
```

### NotesFragment:
```java
loadNotesByStatus()           // Load items for current tab
searchNotes(query)            // Search within current tab
deleteNote(note)              // Soft delete (→ Deleted tab)
restoreNote(note)             // Restore (→ Active tab)
permanentlyDeleteNote()       // Remove from DB forever
```

---

## 📊 DAO Queries

### AssignmentDao:
```java
getPendingAssignments(userId)       // Pending tab
getCompletedAssignments(userId)     // Completed tab
getDeletedAssignments(userId)       // Deleted tab
getAssignmentsByStatus(userId, status)  // Generic
searchAssignments(userId, status, query) // Tab-specific search
```

### NoteDao:
```java
getActiveNotes(userId)          // Active tab
getDeletedNotes(userId)         // Deleted tab
getNotesByStatus(userId, status)    // Generic
searchNotes(userId, status, query)  // Tab-specific search
```

---

## ⚡ Performance Tips

### Efficient Queries:
- ✅ Use `getAssignmentsByStatus()` instead of loading all and filtering
- ✅ Search queries include status filter (indexed)
- ✅ RecyclerView only holds current tab's items

### Database Indexes:
```sql
-- Room automatically creates indexes on foreign keys
-- Status fields should ideally have indexes for performance
```

---

## 🐛 Troubleshooting

### "No items showing in tab"
- ✅ Check: currentStatus variable matches tab
- ✅ Check: Database has items with that status
- ✅ Check: loadByStatus() is called after tab switch

### "Search not working"
- ✅ Check: Search query includes currentStatus
- ✅ Check: TextWatcher is attached to EditText
- ✅ Check: DAO search method has status parameter

### "Tab switch not working"
- ✅ Check: TabLayout listener is set up
- ✅ Check: currentStatus updates in onTabSelected()
- ✅ Check: loadByStatus() is called after status change

---

## 🎓 Code Snippets

### Switch Tab Programmatically:
```java
TabLayout.Tab tab = tabLayout.getTabAt(0); // 0=first tab
if (tab != null) {
    tab.select();
}
```

### Get Current Tab Position:
```java
int position = tabLayout.getSelectedTabPosition();
```

### Soft Delete Pattern:
```java
public void softDelete(Item item) {
    new Thread(() -> {
        item.setStatus("deleted");
        database.dao().update(item);
        requireActivity().runOnUiThread(this::refresh);
    }).start();
}
```

### Restore Pattern:
```java
public void restore(Item item) {
    new Thread(() -> {
        item.setStatus("active"); // or "pending"
        database.dao().update(item);
        requireActivity().runOnUiThread(this::refresh);
    }).start();
}
```

---

## ✅ Testing Checklist

### Assignments:
- [ ] Create assignment → appears in Pending
- [ ] Mark complete → moves to Completed
- [ ] Delete from Pending → moves to Deleted
- [ ] Delete from Completed → moves to Deleted
- [ ] Restore from Deleted → returns to Pending
- [ ] Search in each tab works independently
- [ ] Tab switching is smooth

### Notes:
- [ ] Create note → appears in Active
- [ ] Delete note → moves to Deleted
- [ ] Restore note → returns to Active
- [ ] Search in each tab works independently
- [ ] Tab switching is smooth

---

## 🎉 Quick Start

1. **Run the app**
2. **Go to Assignments tab**
3. **See 3 tabs at top**
4. **Create an assignment** → goes to Pending
5. **Click Complete** → moves to Completed ✅
6. **Click Delete** → moves to Deleted ✅
7. **Switch to Deleted tab** → see deleted item
8. **Click Restore** → back to Pending ✅

**It just works!** 🚀

---

## 📞 Need Help?

Check these files:
- `AssignmentsFragment.java` - Assignment tab logic
- `NotesFragment.java` - Note tab logic
- `fragment_assignments.xml` - Assignment layout with tabs
- `fragment_notes.xml` - Note layout with tabs
- `AssignmentDao.java` - Database queries
- `NoteDao.java` - Database queries

**Everything is documented and ready to use!**

