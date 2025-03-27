export type ContextVariables = {
    db: typeof db;
    user: User | null;
    session: Session | null;
};