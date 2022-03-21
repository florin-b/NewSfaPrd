package my.logon.screen.listeners;

import my.logon.screen.enums.EnumDaNuOpt;
import my.logon.screen.enums.EnumDialogConstraints;

public interface GenericDialogListener {
    void dialogResponse(EnumDialogConstraints constraint, EnumDaNuOpt response);

}
