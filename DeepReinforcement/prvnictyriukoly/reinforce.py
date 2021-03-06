#!/usr/bin/env python3
import numpy as np
import tensorflow as tf

import cart_pole_evaluator

def computeReturns(rewards):
    returns = np.zeros(len(rewards))
    i = 0
    G = 0
    for reward in reversed(rewards):
        G = reward + args.gamma*G
        index = len(returns) - 1 - i
        returns[index] = G
        i += 1
    return returns
        

class Network:
    def __init__(self, threads, seed=42):
        # Create an empty graph and a session
        graph = tf.Graph()
        graph.seed = seed
        self.session = tf.Session(graph = graph, config=tf.ConfigProto(inter_op_parallelism_threads=threads,
                                                                       intra_op_parallelism_threads=threads))

    def construct(self, args, state_shape, num_actions):
        with self.session.graph.as_default():
            self.states = tf.placeholder(tf.float32, [None] + state_shape)
            self.actions = tf.placeholder(tf.int32, [None])
            self.returns = tf.placeholder(tf.float32, [None])

            # TODO: Start with self.states and
            hidden = self.states
            # - add a fully connected layer of size args.hidden_layer and ReLU activation
            hidden = tf.layers.dense(hidden, args.hidden_layer, activation=tf.nn.relu)
            # - add a fully connected layer with num_actions and no activation, computing `logits`
            logits = tf.layers.dense(hidden,num_actions)
            # - compute `self.probabilities` as tf.nn.softmax of `logits`
            self.probabilities = tf.nn.softmax(logits)

            # TODO: Training
            # - compute `loss` as sparse softmax cross entropy of `self.actions` and `logits`,
            # weighted by `self.returns` (using `weights` param)
            loss = tf.losses.sparse_softmax_cross_entropy(self.actions,logits, weights = self.returns)

            global_step = tf.train.create_global_step()
            self.training = tf.train.AdamOptimizer(args.learning_rate).minimize(loss, global_step=global_step, name="training")

            # Initialize variables
            self.session.run(tf.global_variables_initializer())

    def predict(self, states):
        return self.session.run(self.probabilities, {self.states: states})

    def train(self, states, actions, returns):
        self.session.run(self.training, {self.states: states, self.actions: actions, self.returns: returns})

if __name__ == "__main__":
    # Fix random seed
    np.random.seed(42)

    # Parse arguments
    import argparse
    parser = argparse.ArgumentParser()
    parser.add_argument("--batch_size", default=32, type=int, help="Number of episodes to train on.")
    parser.add_argument("--episodes", default=10000, type=int, help="Training episodes.")
    parser.add_argument("--gamma", default=0.99, type=float, help="Discounting factor.")
    parser.add_argument("--hidden_layer", default=20, type=int, help="Size of hidden layer.")
    parser.add_argument("--learning_rate", default=0.2, type=float, help="Learning rate.")
    parser.add_argument("--render_each", default=0, type=int, help="Render some episodes.")
    parser.add_argument("--threads", default=1, type=int, help="Maximum number of threads to use.")
    args = parser.parse_args()

    # Create the environment
    env = cart_pole_evaluator.environment(discrete=False)

    # Construct the network
    network = Network(threads=args.threads)
    network.construct(args, env.state_shape, env.actions)

    # Training
    while True:
        batch_states, batch_actions, batch_returns = [], [], []
        for _ in range(args.batch_size):
            # Perform episode
            states, actions, rewards = [], [], []
            state, done = env.reset(), False
            while not done:
                if args.render_each and env.episode > 0 and env.episode % args.render_each == 0:
                    env.render()

                # TODO: Compute action probabilities using `network.predict` and current `state`
                actionProbab = network.predict([state])

                # TODO: Choose `action` according to `probabilities` distribution (np.random.choice can be used)
                action = np.random.choice(env.actions ,p = actionProbab[0])

                next_state, reward, done, _ = env.step(action)

                states.append(state)
                actions.append(action)
                rewards.append(reward)

                state = next_state

            # TODO: Compute returns by summing rewards (with discounting)
            returns = computeReturns(rewards)

            # TODO: Add states, actions and returns to the training batch
            batch_states.extend(states)
            batch_actions.extend(actions)
            batch_returns.extend(returns)

        # Train using the generated batch
        network.train(batch_states, batch_actions, batch_returns)
        
        if env.episode >= 100:
            avg = np.average(env._episode_returns[-100:])
            if np.average(avg) > 490:
                break


    # Final evaluation
    while True:
        state, done = env.reset(True), False
        while not done:
            # TODO: Compute action `probabilities` using `network.predict` and current `state`
            probabilities = network.predict([state])
            # Choose greedy action this time
            action = np.argmax(probabilities[0])
            state, reward, done, _ = env.step(action)