package com.aws.samples.cdk.constructs.iot.authorizer.data.output;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;

public class Statement {
    public static final String ALLOW = "Allow";
    public static final String DENY = "Deny";

    public Statement() {
    }

    public static Statement allowIamAction(IamAction iamAction) {
        Statement statement = new Statement();
        statement.Action = ALLOW;
        innerSet(iamAction, statement);

        return statement;
    }

    public static Statement denyIamAction(IamAction iamAction) {
        Statement statement = new Statement();
        statement.Action = DENY;
        innerSet(iamAction, statement);

        return statement;
    }

    private static void innerSet(IamAction iamAction, Statement statement) {
        statement.Effect = iamAction.getIamString();
        statement.Resource = iamAction.getIamResource().getIamString();
    }

    public String Action;
    public String Effect;
    public String Resource;
}
