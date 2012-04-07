package lehirtieclipseplugin.renameparticipant;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

public class JavaElementRenameParticipant extends RenameParticipant {
  
  public JavaElementRenameParticipant() {
  }
  
  @Override
  public RefactoringStatus checkConditions(final IProgressMonitor arg0, final CheckConditionsContext arg1)
      throws OperationCanceledException {
    return null;
  }
  
  @Override
  public Change createChange(final IProgressMonitor arg0) throws CoreException, OperationCanceledException {
    final Object oldElement = getProcessor().getElements()[0];
    final String newName = getArguments().getNewName();
    return null;
  }
  
  @Override
  public String getName() {
    return "LehirtiRefactorRenameParticipant";
  }
  
  @Override
  protected boolean initialize(final Object arg0) {
    return getType(arg0) != null;
  }
  
  private Object getType(final Object arg) {
    if (arg instanceof IField) {
      final IField field = (IField) arg;
      try {
        if (field.isEnumConstant()) {
          final IJavaElement javaElement = field.getParent();
          final IJavaElement parent = javaElement.getParent();
          final IResource correspondingResource = parent.getCorrespondingResource();
          final int i = 0;
        }
      } catch (final JavaModelException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    return null; // we are not interested in this rename operation
  }
}
