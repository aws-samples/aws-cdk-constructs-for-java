package com.aws.samples.cdk.constructs.iot.authorizer.data.output;

import com.aws.samples.cdk.constructs.iam.permissions.IamAction;

public class Statement {
    public static final String ALLOW = "Allow";
    public static final String DENY = "Deny";
    public String Action;
    public String Effect;
    public String Resource;

    public Statement() {
    }

    public static Statement allowIamAction(IamAction iamAction) {
        Statement statement = new Statement();
        statement.Effect = ALLOW;
        innerSet(iamAction, statement);

        return statement;
    }

    public static Statement denyIamAction(IamAction iamAction) {
        Statement statement = new Statement();
        statement.Effect = DENY;
        innerSet(iamAction, statement);

        return statement;
    }

    private static void innerSet(IamAction iamAction, Statement statement) {
        statement.Action = iamAction.getIamString();
        statement.Resource = iamAction.getIamResource().getIamString();
    }
}
